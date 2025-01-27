/*
 * Copyright (c) 2018-2021 NetFoundry, Inc.
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

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("maven-publish")
    alias(libs.plugins.shadow)
}

ext {
    description = "Ziti JDBC (ZDBC) wrapper"
}

dependencies {
    implementation(project(":ziti"))
}

java {
    withSourcesJar()
}

tasks.register<Jar>("packageJavadoc") {
    from(tasks.javadoc.get().destinationDir)
    archiveClassifier.set("javadoc")
}

tasks.named<ShadowJar>("shadowJar") {
    manifest.from(tasks.jar.get().manifest)
    archiveClassifier.set("full")
    configurations = listOf(project.configurations.runtimeClasspath.get())
    mergeServiceFiles()
    dependsOn(":ziti:jar")
}

tasks.named<Jar>("jar") {
    manifest {
        attributes("Main-Class" to "org.openziti.jdbc.CommandLine")
    }
}

publishing {
    publications {
        create<MavenPublication>(project.name) {
            from(components["java"])
            artifact(tasks["packageJavadoc"])
        }
    }
}

apply(from = rootProject.file("publish.gradle"))

