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
import java.io.ByteArrayOutputStream


val zitiBuildnum by extra { System.getenv("BUILD_NUMBER") ?: "local" }


buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.6.1")
        classpath("org.jfrog.buildinfo:build-info-extractor-gradle:4.14.1")
    }
}

plugins {
    kotlin("jvm") version "1.3.61"
    id("io.wusa.semver-git-plugin") version "2.0.2"
    id("com.jfrog.artifactory") version "4.14.1"
}

repositories {
    mavenCentral()
    google()
}


group = "io.netfoundry.ziti"

semver {
    //nextVersion = "patch"
    snapshotSuffix = "pre"
    initialVersion = "0.1.0"
    branches {
        branch {
            regex = "master"
            incrementer = "PATCH_INCREMENTER"
            formatter = Transformer { info -> "${info.version.major}.${info.version.minor}.${info.version.patch}-${zitiBuildnum}" }
        }
        branch {
            regex = ".+"
            incrementer = "NO_VERSION_INCREMENTER"
            formatter = Transformer { info ->
                val v = info.version
                """${info.branch.id}-${v.major}.${v.minor}.${v.patch}.${v.suffix?.count ?: "0"}.${v.suffix?.sha}""" }
        }
    }
}

val gitInfo by extra { semver.info }
val gitCommit by extra { semver.info.shortCommit }
val gitBranch by extra { semver.info.branch.name }

version = semver.info.version
println("version = ${version}")

subprojects {
    group = rootProject.group
    version = rootProject.version

    plugins.apply("com.jfrog.artifactory")

    tasks.withType<KotlinCompile>().all {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    tasks.withType<PublishToMavenRepository>().all {
        onlyIf { !gitInfo.dirty }
    }
}

fun Project.sh(cmd: String, vararg args: String): String {
    val out = ByteArrayOutputStream()
    this.exec {
        commandLine(cmd, *args)
        standardOutput = out
    }
    return out.toString()
}

tasks.register("tagIfNeeded") {
    if (gitBranch == "master") {

        doLast {
            val zitiVer = File("version").readText().trim()

            val (zitiMajor, zitiMinor, zitiPatch) = zitiVer.split(".").map { it.toInt() }

            println ("${zitiMajor} -> ${zitiMinor} -> ${zitiPatch}")

            val tagVer = project.sh("git", "describe", "--long")

            println(tagVer)

            val tagVerSplit = tagVer.split(regex = Regex("[\\.-]")).take(4)
            val tagMajor = tagVerSplit[0].toInt()
            val tagMinor = tagVerSplit[1].toInt()
            val tagPatch = tagVerSplit[2].toInt()
            val ahead = tagVerSplit[3].toInt()

            if ( zitiMajor > tagMajor ||
                (zitiMajor == tagMajor && zitiMinor > tagMinor ) ||
                (zitiMinor == tagMajor && zitiMinor == tagMinor && zitiPatch > tagPatch) )
            {
                val new_tag = zitiVer
                println("advancing tag($new_tag) based on 'version' file")
                project.sh("git", "tag",  "-a",  new_tag, "-m", "CI tag ${new_tag}")
            } else {
                if (ahead == 0) {
                    println("already has tag = ${tagMajor}.${tagMinor}.${tagPatch}")
                    // new_tag = "${tagMajor}.${tagMinor}.${tagPatch}"
                }
                else {
                    println("bumping up new tag")
                    val new_tag = "${tagMajor}.${tagMinor}.${tagPatch + 1}"
                    println("setting new tag = $new_tag")
                    project.sh("git", "tag",  "-a",  new_tag, "-m", "CI tag ${new_tag}")

                }
            }

        }
    }
}
