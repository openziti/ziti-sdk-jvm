import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

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

    implementation(libs.tls.channel)
    implementation(libs.okhttp3)

    implementation(libs.jjwt.api)
    implementation(libs.jjwt.impl)
    implementation(libs.jjwt.gson)

    implementation("io.dropwizard.metrics:metrics-core:4.2.30")
    implementation("org.bouncycastle:bcpkix-jdk18on:1.80")

    implementation(libs.sodium) {
        exclude(module = "slf4j-api")
    }

    testApi(libs.jupiter.api)
    testImplementation(libs.jupiter.engine)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.slf4j.simple)
}

val generatedResourcesDir = "${buildDir}/generated-resources/main"

val gitCommit = rootProject.ext["gitCommit"]
val gitBranch = rootProject.ext["gitBranch"]

tasks.register<WriteProperties>("versionProps") {
    destinationFile = file("${generatedResourcesDir}/org/openziti/util/ziti-version.properties")

    property("version", "${project.version}")
    property("revision", "$gitCommit")
    property("branch", "$gitBranch")
}

sourceSets.main {
    resources.srcDir(files(generatedResourcesDir).builtBy(tasks["versionProps"]))
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

publishing {
    publications {
        create<MavenPublication>(project.name) {
            from(components["java"])
            artifact(tasks["dokkaJar"])
        }
    }
}

apply(from = rootProject.file("publish.gradle"))
