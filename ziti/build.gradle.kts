import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

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
    alias(libs.plugins.kotlin.serialization)
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
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.slf4j.api)
    implementation(libs.gson)
    implementation(libs.jackson.bind)
    implementation(libs.jackson.kotlin)

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
    manifest.inheritFrom(tasks.jar.get().manifest)
    archiveClassifier.set("full")
    mergeServiceFiles()
    configurations = listOf(project.configurations.runtimeClasspath.get())
}

tasks.register<Jar>("dokkaJar") {
    dependsOn(tasks.dokkaJavadoc)
    archiveClassifier.set("javadoc")
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
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

val zitiVersion = libs.versions.ziti.cli.get()
val binDir = layout.buildDirectory.dir("bin").get()
val zitiCLI = binDir.file("ziti")
val quickstartHome = layout.buildDirectory.dir("quickstart").get()

tasks.register<Exec>("buildZiti") {
    group = LifecycleBasePlugin.BUILD_GROUP
    description = "Builds the Ziti CLI"
    environment("GOBIN", binDir.asFile.absolutePath)
    commandLine("go", "install", "github.com/openziti/ziti/ziti@v${zitiVersion}")
    outputs.file(zitiCLI)
}

tasks.register("start-quickstart") {
    description = "Starts Ziti quickstart"
    val qsLog = layout.buildDirectory.file("quickstart.log").get().asFile
    val errLog = layout.buildDirectory.file("error.log").get().asFile

    doLast {
        val pb = ProcessBuilder().apply {
            command(
                zitiCLI.toString(),
                "edge", "quickstart", "--verbose", "--home", quickstartHome.asFile.absolutePath)
            redirectOutput(qsLog)
            redirectError(errLog)
        }
        val qsProc = pb.start()
        ext["quickstart"] = qsProc
        runBlocking {

            while(true) {
                delay(1000)
                if (!qsProc.isAlive) {
                    throw GradleException("quickstart failed: check ${errLog.absolutePath}")
                }

                val started = kotlin.runCatching {
                    qsLog.readLines().find { it.contains("controller and router started") }
                }
                if (started.getOrNull() != null) {
                    logger.lifecycle("quickstart is ready")
                    break
                }

                logger.lifecycle("waiting for qs router...")
            }
        }
    }
    dependsOn("integrationTestClasses", "buildZiti")
}

tasks.register("stop-quickstart") {
    doLast {
        val proc = ext["quickstart"] as Process?
        proc?.let {
            logger.lifecycle("stopping quickstart...")
            it.destroy()
        }
    }
}

tasks.named("integrationTest") {
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
