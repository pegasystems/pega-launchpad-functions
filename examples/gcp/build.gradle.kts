plugins {
    id("java")
}

// set flags for shared dependencies
extra["useCommonTestLibraries"] = true
extra["useAnnotations"] = true

apply(from = rootProject.file("gradle/common-dependencies.gradle.kts"))

group = "com.pega.launchpad.gcp"
version = extra["PegaLaunchpadFunctionsGroupVersion"].toString() + "-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = extra["PegaLaunchpadFunctionsJunitVersion"].toString()

dependencies {
    implementation(platform("com.google.cloud:libraries-bom:26.88.0"))
    implementation(platform("com.fasterxml.jackson:jackson-bom:2.22.2"))
    implementation("com.google.cloud:google-cloud-storage")
    implementation("com.google.auth:google-auth-library-credentials")
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

extra["resourceCenterArtifactTask"] = "jar"
extra["resourceCenterArtifactTarget"] = "gcp.jar"
apply(from = rootProject.file("gradle/resourcecenter-artifact.gradle.kts"))