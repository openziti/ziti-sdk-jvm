/*
 * Copyright (c) 2018-2026 NetFoundry Inc.
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

import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import org.gradle.process.ExecOperations
import java.io.ByteArrayOutputStream
import javax.inject.Inject

/*
 * Derives the build version and git details from the repository state.
 *
 * Replaces io.wusa.semver-git-plugin, which started git processes while the build was being configured and
 * so could not work with the Gradle configuration cache. The rules are the ones that plugin was configured with
 * here: lightweight tags, the next version is the last tag with its patch number incremented,
 * `main` gets a plain `x.y.z` version and other branches get `x.y.z-<branch>-<commits>.<sha>`.
 * Anything not on a tag is a -SNAPSHOT, and a dirty working tree adds `-dirty`.
 *
 * Everything is computed inside one ValueSource, so the cache entry is only invalidated when one of the
 * resulting values changes, not whenever a file is edited.
 *
 * Exposes extra properties: gitVersion, gitCommit, gitBranch, gitDirty.
 */
abstract class GitVersionSource : ValueSource<List<String>, GitVersionSource.Params> {

    interface Params : ValueSourceParameters {
        val workingDir: DirectoryProperty
    }

    @get:Inject
    abstract val exec: ExecOperations

    private data class SemVer(
        val major: Int,
        val minor: Int,
        val patch: Int,
        val prerelease: String = "",
        val build: String = "",
        val suffixCount: Int? = null,
        val suffixSha: String? = null,
    ) {
        val core get() = "$major.$minor.$patch"
        val onTag get() = suffixCount == null
    }

    private class BranchRule(val regex: Regex, val increment: (SemVer) -> SemVer, val format: (SemVer, String) -> String)

    private val rules = listOf(
        BranchRule(Regex("main"), { it.copy(patch = it.patch + 1) }) { v, _ -> v.core },
        BranchRule(Regex(".+"), { it.copy(patch = it.patch + 1) }) { v, branch ->
            "${v.core}-${branch.replace("/", "-")}-${v.suffixCount ?: 0}.${v.suffixSha}"
        },
    )

    override fun obtain(): List<String> {
        val count = git("rev-list", "--count", "HEAD")?.toIntOrNull() ?: 0
        val shortCommit = git("rev-parse", "--short", "HEAD")
        val dirty = !git("status", "-s").isNullOrBlank()
        val branch = git("log", "-n", "1", "--pretty=%d", "HEAD")?.let {
            Regex("""\([grafted, ]{0,9}HEAD -> (.*?)[,|)]""").find(it)?.groupValues?.get(1)
        } ?: ""

        val currentTag = git("describe", "--tags", "--exact-match", "--match", "*")
        val version = if (currentTag != null && !dirty) {
            parse(currentTag)
        } else {
            val lastTag = git("describe", "--tags", "--abbrev=7", "--match", "*")
            when {
                lastTag != null -> increment(parse(lastTag), branch)
                shortCommit != null -> SemVer(0, 1, 0, suffixCount = count, suffixSha = shortCommit)
                else -> SemVer(0, 1, 0)
            }
        }

        return listOf(format(version, branch, count, shortCommit, dirty), shortCommit ?: "none", branch, dirty.toString())
    }

    private fun git(vararg args: String): String? {
        val stdout = ByteArrayOutputStream()
        val result = exec.exec {
            commandLine("git", *args)
            workingDir = parameters.workingDir.get().asFile
            standardOutput = stdout
            errorOutput = ByteArrayOutputStream()
            isIgnoreExitValue = true
        }
        return if (result.exitValue == 0) stdout.toString(Charsets.UTF_8).trim() else null
    }

    // `git describe` output: a semantic version tag, optionally followed by -<commits since>-g<sha>
    private fun parse(describe: String): SemVer {
        val suffix = SUFFIX.find(describe)
        val tag = if (suffix != null) SUFFIX.replace(describe, "") else describe
        val (major, minor, patch, prerelease, build) = (SEMVER.matchEntire(tag)
            ?: throw GradleException("'$describe' is not a semantic version tag")).destructured

        return SemVer(
            major.toInt(), minor.toInt(), patch.toInt(), prerelease, build,
            suffix?.groupValues?.get(1)?.toInt(), suffix?.groupValues?.get(2)
        )
    }

    private fun increment(version: SemVer, branch: String): SemVer =
        rules.firstOrNull { it.regex.matches(branch) }?.increment?.invoke(version)
            // no rule for this branch, e.g. a detached HEAD
            ?: version.copy(minor = version.minor + 1, patch = 0)

    private fun format(version: SemVer, branch: String, count: Int, shortCommit: String?, dirty: Boolean): String {
        if (count == 0) return "${version.core}-SNAPSHOT"

        if (version.onTag && !dirty) {
            return version.core +
                    (if (version.prerelease.isNotEmpty()) "-${version.prerelease}" else "") +
                    (if (version.build.isNotEmpty()) "+${version.build}" else "")
        }

        val dirtyMarker = if (dirty) "-dirty" else ""
        val formatted = rules.firstOrNull { it.regex.matches(branch) }?.format?.invoke(version, branch)
            ?: "${version.core}+build.$count.sha.${shortCommit ?: "none"}"

        return "$formatted$dirtyMarker-SNAPSHOT"
    }

    private companion object {
        val SUFFIX = Regex("""(?:-([0-9]+)(?:-g([0-9a-f]{1,7}))(-dirty)?)$""")
        val SEMVER = Regex(
            """^[vV]?(0|[1-9]\d*)\.(0|[1-9]\d*)\.(0|[1-9]\d*)""" +
                    """(?:-((?:0|[1-9]\d*|\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\.(?:0|[1-9]\d*|\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?""" +
                    """(?:\+([a-zA-Z0-9][a-zA-Z0-9\.-]+)?)?$"""
        )
    }
}

val gitInfo = providers.of(GitVersionSource::class) {
    parameters.workingDir.set(rootProject.layout.projectDirectory)
}.get()

extra["gitVersion"] = gitInfo[0]
extra["gitCommit"] = gitInfo[1]
extra["gitBranch"] = gitInfo[2]
extra["gitDirty"] = gitInfo[3].toBoolean()
