import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.util.Properties

/*
 * Copyright (c) 2018-2021 NetFoundry Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    id("java-library")
    alias(libs.plugins.kotlin)
    alias(libs.plugins.dokka)
    id("maven-publish")
    alias(libs.plugins.shadow)
    alias(libs.plugins.download)
}

ext {
    description = "Ziti SDK for JVM"
}

dependencies {
    api(project(":edge-api"))

    implementation(libs.kotlin.lib)
    implementation(libs.kotlin.coroutines.lib)
    implementation(libs.kotlin.reflect)
    implementation(libs.slf4j.api)
    implementation(libs.gson)
    implementation(libs.jackson.bind)
    implementation(libs.jackson.kotlin)
    // needed by the mapper for the generated edge models (edge-api only exposes them at runtime)
    implementation(libs.jackson.datatype)
    implementation(libs.jackson.databind.nullable)

    implementation(libs.tls.channel)
    implementation(libs.okhttp3)

    implementation(libs.jjwt.api)
    implementation(libs.jjwt.impl)
    implementation(libs.jjwt.gson)
    implementation(libs.protobuf)
    implementation(libs.metrics)
    implementation(libs.bouncycastle)

    implementation(libs.sodium) {
        exclude(module = "slf4j-api")
    }

    testApi(libs.jupiter.api)
    testImplementation(libs.jupiter.engine)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.slf4j.simple)
    testImplementation(libs.okhttp3.mockwebserver3)
    testImplementation(libs.okhttp3.tls)
}

val generatedResourcesDir = "${layout.buildDirectory.get()}/generated-resources/main"

val gitCommit = rootProject.ext["gitCommit"]
val gitBranch = rootProject.ext["gitBranch"]

tasks.register<WriteProperties>("versionProps") {
    destinationFile = file("${generatedResourcesDir}/org/openziti/util/ziti-version.properties")

    property("version", "${project.version}")
    property("revision", "$gitCommit")
    property("branch", "$gitBranch")
}


sourceSets {
    main {
        resources.srcDir(files(generatedResourcesDir).builtBy(tasks["versionProps"]))
    }

    val samples by creating {
        java.srcDir("src/samples/java")
        kotlin.srcDir("src/samples/kotlin")
        compileClasspath += sourceSets.main.get().runtimeClasspath
        runtimeClasspath += sourceSets.main.get().runtimeClasspath
        dependencies {
            implementation(libs.slf4j.simple)
            implementation(libs.clikt)
        }
    }
}

tasks.register<Exec>("updateProtobuf") {
    group = LifecycleBasePlugin.BUILD_GROUP
    description = "updates generated protobuf sources in src/main/java. requires `protoc` on your path"
    val protoDir = layout.buildDirectory.dir("proto")
    download.run {
        src("https://raw.githubusercontent.com/openziti/sdk-golang/refs/heads/main/pb/edge_client_pb/edge_client.proto")
        dest(protoDir)
    }

    commandLine("protoc",
        "-I", protoDir.get().asFile.absolutePath,
        "--java_out=lite:src/main/java",
        "edge_client.proto"
    )
}

java {
    withSourcesJar()
}

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("full")
    mergeServiceFiles()
    configurations = listOf(project.configurations.runtimeClasspath.get())
}

tasks.register<Jar>("dokkaJar") {
    dependsOn(tasks.dokkaGeneratePublicationJavadoc)
    archiveClassifier.set("javadoc")
    from(tasks.dokkaGeneratePublicationJavadoc.flatMap { it.outputDirectory })
}

@Suppress("UnstableApiUsage")
testing {
    suites {
        val integrationTest by registering(JvmTestSuite::class) {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlin.coroutines.lib)
                implementation(libs.kotlin.coroutines.test)
                implementation(libs.slf4j.simple)
                implementation(project(":management-api"))
            }
        }
    }
}

kotlin {
    target {
        // make sure we can test internal components
        compilations.getByName("integrationTest").associateWith(compilations.getByName("main"))
    }
}

// Owns the quickstart process so the tasks below only hold a reference to the service, which the
// configuration cache can handle. Gradle also closes it when the build ends, so a failed build can't leave it running.
abstract class QuickstartService : BuildService<QuickstartService.Params>, AutoCloseable {
    interface Params : BuildServiceParameters {
        val home: DirectoryProperty
        val log: RegularFileProperty
        val errorLog: RegularFileProperty
        // quickstart defaults are used when unset; see local.properties below
        val ctrlPort: Property<String>
        val routerPort: Property<String>
    }

    private var process: Process? = null

    val errorLogPath: String get() = parameters.errorLog.get().asFile.absolutePath

    @Synchronized
    fun start() {
        val cmd = mutableListOf("ziti", "edge", "quickstart", "--verbose", "--home", parameters.home.get().asFile.absolutePath)
        parameters.ctrlPort.orNull?.let { cmd += listOf("--ctrl-port", it) }
        parameters.routerPort.orNull?.let { cmd += listOf("--router-port", it) }

        process = ProcessBuilder().apply {
            command(cmd)
            redirectOutput(parameters.log.get().asFile)
            redirectError(parameters.errorLog.get().asFile)
        }.start()
    }

    @Synchronized
    fun isAlive() = process?.isAlive ?: false

    fun isReady() = runCatching {
        parameters.log.get().asFile.readLines().any { it.contains("controller and router started") }
    }.getOrDefault(false)

    // returns true if there was a running quickstart to stop
    @Synchronized
    fun stop(): Boolean {
        val running = process ?: return false
        process = null
        running.destroy()
        return true
    }

    override fun close() {
        stop()
    }
}

// Optional overrides for the quickstart ports, to run the integration tests while another controller is using the
// defaults (1280 and 3022). Put them in local.properties, which is git-ignored, in the root of the repository:
//     ziti.quickstart.ctrl.port=11280
//     ziti.quickstart.router.port=13022
val localProperties = providers.fileContents(rootProject.layout.projectDirectory.file("local.properties"))
    .asText.map { text -> Properties().apply { load(text.reader()) } }

fun localProperty(name: String): Provider<String> = localProperties.map { it.getProperty(name) }

val quickstart = gradle.sharedServices.registerIfAbsent("quickstart", QuickstartService::class) {
    parameters.home.set(layout.buildDirectory.dir("quickstart"))
    parameters.log.set(layout.buildDirectory.file("quickstart.log"))
    parameters.errorLog.set(layout.buildDirectory.file("error.log"))
    parameters.ctrlPort.set(localProperty("ziti.quickstart.ctrl.port"))
    parameters.routerPort.set(localProperty("ziti.quickstart.router.port"))
}

tasks.register("start-quickstart") {
    description = "Starts Ziti quickstart"
    // local copy: the action must capture the service, not the build script
    val service = quickstart
    usesService(service)

    doLast {
        val qs = service.get()
        qs.start()
        while (true) {
            Thread.sleep(1000)
            if (!qs.isAlive()) {
                throw GradleException("quickstart failed: check ${qs.errorLogPath}")
            }
            if (qs.isReady()) {
                logger.lifecycle("quickstart is ready")
                break
            }
            logger.lifecycle("waiting for qs router...")
        }
    }
    dependsOn("integrationTestClasses")
}

tasks.register("stop-quickstart") {
    val service = quickstart
    usesService(service)

    doLast {
        if (service.get().stop()) {
            logger.lifecycle("stopping quickstart...")
        }
    }
}

tasks.named<Test>("integrationTest") {
    // the tests must look for the controller on the port the quickstart was started with
    localProperty("ziti.quickstart.ctrl.port").orNull?.let { systemProperty("ziti.quickstart.ctrl.port", it) }
    // seamless mode (SeamlessTests) replaces the JVM's socket implementation for good
    forkEvery = 1
    dependsOn("start-quickstart")
    finalizedBy("stop-quickstart")
}

tasks.check.configure {
    dependsOn("integrationTest")
}

publishing {
    publications {
        create<MavenPublication>(project.name) {
            from(components["java"])
            artifact(tasks["dokkaJar"])
        }
    }
}

apply(from = rootProject.file("publish.gradle"))
