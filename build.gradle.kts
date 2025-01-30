import io.wusa.Info
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.text.SimpleDateFormat
import java.util.*

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
    alias(libs.plugins.kotlin).apply(false)
    alias(libs.plugins.nexus.publish)
    alias(libs.plugins.semver.git)
}

semver {
    initialVersion = "0.1.0"
    tagType = io.wusa.TagType.LIGHTWEIGHT
    branches {
        branch {
            regex = "main"
            incrementer = "PATCH_INCREMENTER"
            formatter = Transformer<Any, Info> { info ->
                "${info.version.major}.${info.version.minor}.${info.version.patch}"
            }
        }
        branch {
            regex = ".+"
            incrementer = "PATCH_INCREMENTER"
            formatter = Transformer<Any, Info> { info ->
                val v = info.version
                """${v.major}.${v.minor}.${v.patch}-${info.branch.id}-${v.suffix?.count ?: "0"}.${v.suffix?.sha}"""
            }
        }
    }
}

val gitCommit = semver.info.shortCommit
ext {
    set("gitCommit", semver.info.shortCommit)
    set("gitBranch", semver.info.branch.name)
}

group = "org.openziti"
version = "${semver.info}"

println("${project.name}: ${project.version}")

subprojects {
    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
    }

    tasks.withType<PublishToMavenRepository>().configureEach {
        onlyIf { !semver.info.dirty }
    }

    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    tasks.withType<Jar>().configureEach {
        manifest {
            attributes(
                "Built-By"       to System.getProperty("user.name"),
                    "Build-Timestamp" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(Date()),
                    "Build-Revision" to gitCommit,
                    "Implementation-Version" to "${project.version}",
                    "Created-By"     to "Gradle ${gradle.gradleVersion}",
                    "Build-Jdk"      to "${System.getProperty("java.version")} (${System.getProperty("java.vendor")} ${System.getProperty("java.vm.version")})",
                    "Build-OS"       to "${System.getProperty("os.name")} ${System.getProperty("os.arch")} ${System.getProperty("os.version")}",
                    "Specification-Vendor" to "OpenZiti",
                    "Implementation-Vendor" to "OpenZiti",
            )
        }
    }
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}


