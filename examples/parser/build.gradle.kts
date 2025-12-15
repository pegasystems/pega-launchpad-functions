plugins {
    id("java")
}

// set flags for shared dependencies
extra["useCommonTestLibraries"] = true
extra["useAnnotations"] = true
extra["useGson"] = true

apply(from = rootProject.file("gradle/common-dependencies.gradle.kts"))

//parser-specific libs
group = "com.pega.launchpad.parser"
version = extra["PegaLaunchpadFunctionsGroupVersion"].toString() + "-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = extra["PegaLaunchpadFunctionsJunitVersion"].toString()

dependencies {
    implementation("org.apache.commons:commons-csv:1.14.1")
    // gson will be provided by shared script via useGson flag
}

// Ensure module compiles with Java 11
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes(mapOf("Implementation-Title" to project.name,
            "Implementation-Version" to project.version))
    }
    into("lib") {
        from(configurations.runtimeClasspath)
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    shouldRunAfter(tasks.build)
}