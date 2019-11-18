/*
 * Copyright (c) 2019 NetFoundry, Inc.
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
    id("application")
    id("org.jetbrains.kotlin.jvm")
    id("maven")
    id("maven-publish")
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile(kotlin("reflect"))

    compile(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = "1.3.1")

    compile("com.squareup.retrofit2:retrofit:2.6.0")
    compile("com.squareup.retrofit2:converter-gson:2.6.0")
    compile("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")

    compile("io.jsonwebtoken:jjwt-api:0.10.7")
    compile("io.jsonwebtoken:jjwt-impl:0.10.7")
    compile("io.jsonwebtoken:jjwt-orgjson:0.10.7")

    compile(group = "org.bouncycastle", name = "bcpkix-jdk15on", version = "1.62")

    implementation("com.github.ajalt:clikt:2.2.0")

    testCompile(kotlin("test-junit"))
}

application {
    mainClassName = "io.netfoundry.ziti.identity.Enroller"
    applicationName = "ziti-enroller"
}

val gitCommit: String by rootProject.extra
val gitBranch: String by rootProject.extra

tasks {

    val generatedResourcesDir = buildDir.resolve("generated-resources/main")

    val versionProps by registering(WriteProperties::class) {
        outputFile = generatedResourcesDir.resolve("io/netfoundry/ziti/util/ziti-version.properties")

        property("version", "${project.version}")
        property("revision", gitCommit)
        property("branch", gitBranch)
    }

    sourceSets.main {
        resources.srcDir(files(generatedResourcesDir).builtBy(versionProps))
    }
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val repo: String by extra {
    rootProject.properties["publish.repo"]?.toString() ?: "file://${buildDir}/maven-repo"
}

publishing {
    repositories {
        maven {
            name = "BuildRepo"
            url = uri(repo)
        }
    }
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar.get())

            val distZip by tasks
            artifact(distZip)
        }
    }
}
