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
    alias(libs.plugins.dokka)
    id("java")
    id("maven-publish")
}

ext {
    description = "Ziti adapter for Spring Boot Client(s)"
}

repositories {
    mavenCentral()
}

val springbootVersion = libs.versions.springboot.get()
val lombokVersion = libs.versions.lombok.get()

dependencies {
    implementation("org.springframework.boot:spring-boot:${springbootVersion}")
    implementation("org.springframework.boot:spring-boot-autoconfigure:${springbootVersion}")
    implementation("org.springframework.boot:spring-boot-starter-web:${springbootVersion}")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.5.1")
    implementation("commons-beanutils:commons-beanutils:1.11.0")

    compileOnly("org.projectlombok:lombok:${lombokVersion}")
    annotationProcessor("org.projectlombok:lombok:${lombokVersion}")

    implementation(project(":ziti"))
}

java {
    withSourcesJar()
}

tasks.register<Jar>("dokkaJar") {
    dependsOn(tasks.dokkaJavadoc)
    archiveClassifier.set("javadoc")
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
}


publishing {
    publications {
        create<MavenPublication>("zitiNetty") {
            from(components["java"])
            artifact(tasks["dokkaJar"])
        }
    }
}

apply(from = rootProject.file("publish.gradle"))
