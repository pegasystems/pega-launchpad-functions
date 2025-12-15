plugins {
    id("java")
}

// set flags for shared dependencies
extra["useCommonTestLibraries"] = true
extra["useAnnotations"] = true
extra["useGson"] = true

apply(from = rootProject.file("gradle/common-dependencies.gradle.kts"))

group = "com.pega.launchpad.geocoding"
version = extra["PegaLaunchpadFunctionsGroupVersion"].toString() + "-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = extra["PegaLaunchpadFunctionsJunitVersion"].toString()

dependencies {
    implementation("org.apache.httpcomponents.client5:httpclient5:5.4.4")
    // gson is provided by shared script via useGson flag
    implementation("com.google.maps:google-maps-services:2.2.0")
    implementation("org.slf4j:slf4j-simple:2.0.17")
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