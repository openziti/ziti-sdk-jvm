
plugins {
    id("java")
    id("maven-publish")
    alias(libs.plugins.openapi)
    alias(libs.plugins.spotless)
}

// Some text from the schema is copy/pasted into the source files as UTF-8
// but the default still seems to be to use platform encoding
tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = "11"
    targetCompatibility = "11"

    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>().configureEach {
    options.encoding = "UTF-8"
}

java {
    withSourcesJar()
}

tasks.register<Jar>("javadocJar") {
    archiveClassifier = "javadoc"
    from(tasks.javadoc.get().destinationDir)
    dependsOn(tasks.javadoc)
}


publishing {
    publications {
        create<MavenPublication>(project.name) {
            artifactId = "edge-api"
            from(components["java"])
            artifact(tasks["javadocJar"])
        }
    }
}

val jacksonVersion = libs.versions.jackson.get()
val jakartaAnnotationVersion = libs.versions.jakarta.annotation.get()

ext {
    description = "OpenZiti Edge API client"
}

dependencies {
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    implementation(libs.jackson.core)
    implementation(libs.jackson.annotations)
    implementation(libs.jackson.bind)
    implementation(libs.jackson.datatype)
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")
    implementation("jakarta.annotation:jakarta.annotation-api:$jakartaAnnotationVersion")
}


// generate Ziti Edge API client
// only needed if new version was published in github.com/openziti/edge-api
// run `./gradlew :openApiGenerate`, check build, commit, push
val edgeApiVersion = libs.versions.ziti.api.get()

openApiGenerate {
    applyDefaults()

    remoteInputSpec.set("https://raw.githubusercontent.com/openziti/edge-api/v${edgeApiVersion}/client.yml")
    version.set(project.version.toString())
    outputDir.set("$projectDir")
    generatorName.set("java")
    groupId.set("org.openziti")
    id.set("edge-api")
    modelPackage.set("org.openziti.edge.model")
    apiPackage.set("org.openziti.edge.api")
    generateModelTests.set(false)
    generateApiTests.set(false)
    configOptions = mapOf(
        "dateLibrary" to "java8",
        "library" to "native",
        "asyncNative" to true.toString(),
    )
}


tasks.named("openApiGenerate").get().finalizedBy(":edge-api:spotlessApply")
// Use spotless plugin to automatically format code, remove unused import, etc
// To apply changes directly to the file, run `gradlew spotlessApply`
// Ref: https://github.com/diffplug/spotless/tree/main/plugin-gradle
spotless {
    // comment out below to run spotless as part of the `check` task
//    enforceCheck = false
    format("misc") {
        // define the files (e.g. '*.gradle', '*.md') to apply `misc` to
        target(".gitignore")
        // define the steps to apply to those files
        trimTrailingWhitespace()
        leadingTabsToSpaces() // Takes an integer argument if you don't like 4
        endWithNewline()
    }
    java {
        // don't need to set target, it is inferred from java
        // apply a specific flavor of google-java-format
        googleJavaFormat("1.22.0").aosp().reflowLongStrings()
        removeUnusedImports()
        importOrder()
    }
}


tasks.named("spotlessApply").get().mustRunAfter("openApiGenerate")
tasks.named("spotlessJava").get().mustRunAfter("openApiGenerate")
tasks.named("spotlessMisc").get().mustRunAfter("openApiGenerate")

apply(from = rootProject.file("publish.gradle"))


