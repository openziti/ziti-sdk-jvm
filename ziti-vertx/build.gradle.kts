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

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.dokka)
    `maven-publish`
}

ext {
    description = "vert.x support for Ziti"
}

java {
    withSourcesJar()
}

sourceSets {
    create("samples") {
        kotlin {
            compileClasspath += main.get().output + main.get().compileClasspath
            runtimeClasspath += output + compileClasspath
        }
    }
}

dependencies {
    api(project(":ziti"))
    implementation(project(":ziti-netty"))

    implementation(libs.kotlin.lib)
    implementation("io.vertx:vertx-core:4.5.12")

    testApi(libs.jupiter.api)
    testImplementation(libs.jupiter.engine)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.gson)

    "samplesImplementation"(libs.slf4j.simple)
}

tasks.register<Jar>("dokkaJar") {
    dependsOn(tasks.dokkaJavadoc)
    archiveClassifier.set("javadoc")
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
}

publishing {
    publications {
        create<MavenPublication>("zitiVertx") {
            from(components["java"])
            artifact(tasks["dokkaJar"])
        }
    }
}

apply(from = rootProject.file("publish.gradle"))
