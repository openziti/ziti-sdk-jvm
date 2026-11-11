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

package org.openziti

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

// Android has no java.net.http, so the SDK must not use it, nor the generated edge-api client and its
// ApiException, which are built on it. The edge models (org.openziti.edge.model) are fine.
class NoJavaNetHttpGuardTest {

    // a wildcard import of org.openziti.edge would let ApiClient and ApiException appear under their bare names
    private val banned = Regex("""java\.net\.http|org\.openziti\.edge\.(ApiClient|ApiException|api\.|\*)""")

    private fun mainSources(): List<File> =
        listOf("src/main/kotlin", "src/main/java").map(::File).filter { it.isDirectory }
            .flatMap { dir -> dir.walkTopDown().filter { it.isFile && it.extension in setOf("kt", "java") }.toList() }

    private fun isBanned(line: String): Boolean {
        val code = line.trim()
        val isComment = code.startsWith("//") || code.startsWith("*") || code.startsWith("/*")
        return !isComment && banned.containsMatchIn(code.substringBefore("//"))
    }

    @Test
    fun mainSourcesDoNotUseJavaNetHttp() {
        val sources = mainSources()
        // unit tests run in the module directory; a guard that scans nothing guards nothing
        assertTrue("no main sources found from ${File(".").absolutePath}", sources.size > 20)

        val offenders = sources.flatMap { file ->
            file.readLines().mapIndexedNotNull { i, line ->
                if (isBanned(line)) "${file.path}:${i + 1}: ${line.trim()}" else null
            }
        }

        assertTrue("java.net.http or the generated edge client used in main sources:\n" + offenders.joinToString("\n"), offenders.isEmpty())
    }

    @Test
    fun guardFlagsTheGeneratedClientButNotTheModels() {
        val flagged = listOf(
            "import java.net.http.HttpClient",
            "import org.openziti.edge.ApiClient",
            "import org.openziti.edge.ApiException",
            "import org.openziti.edge.api.ServiceApi",
            "import org.openziti.edge.api.*",
            // brings ApiClient and ApiException into scope under their bare names
            "import org.openziti.edge.*",
        )
        val allowed = listOf(
            "import org.openziti.edge.model.*",
            "import org.openziti.edge.model.ServiceDetail",
            "// built on java.net.http",
            " * org.openziti.edge.ApiClient is not used",
        )

        assertEquals(emptyList<String>(), flagged.filterNot(::isBanned))
        assertEquals(emptyList<String>(), allowed.filter(::isBanned))
    }

    // The edge-api jar still contains the generated client, which references java.net.http. R8 fails release
    // builds on missing classes unless told otherwise; it reads rules from META-INF/proguard in library jars.
    @Test
    fun r8RuleIsShipped() {
        val stream = checkNotNull(javaClass.getResourceAsStream("/META-INF/proguard/ziti.pro")) {
            "META-INF/proguard/ziti.pro is not on the classpath"
        }
        val rules = stream.bufferedReader().use { it.readLines() }.map { it.trim() }

        assertTrue(rules.toString(), "-dontwarn java.net.http.**" in rules)
    }
}
