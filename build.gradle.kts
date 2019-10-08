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

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val zitiBuildnum by extra { System.getenv("BITBUCKET_BUILD_NUMBER") ?: "local" }
val publishToken by extra { System.getenv("JFROG_API_KEY") }

plugins {
    kotlin("jvm") version "1.3.50"
    id("io.wusa.semver-git-plugin") version "1.2.0"
}

repositories {
    mavenCentral()
}


group = "io.netfoundry.ziti"

semver {
    nextVersion = "patch"
    snapshotSuffix = "${zitiBuildnum}<dirty>"
    dirtyMarker = "-dirty"
    initialVersion = "0.1.0"
}

var versionPrefix = ""
var versionSuffix = "-${zitiBuildnum}"

if (semver.info.branch.id != "master") {
    versionPrefix = "${semver.info.branch.id}-"
    versionSuffix = ""
}
version = "${versionPrefix}${semver.info.version}${versionSuffix}"
val gitCommit by extra { semver.info.shortCommit }
val gitBranch by extra { semver.info.branch.name }

subprojects {
    version = rootProject.version
    val gitCommit by extra { rootProject.semver.info.shortCommit }
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}