plugins {
    id("java")
}

// set flags for shared dependencies
extra["useCommonTestLibraries"] = true
extra["useGson"] = true

apply(from = rootProject.file("gradle/common-dependencies.gradle.kts"))

group = "com.pega.launchpad.oracle"
version = extra["PegaLaunchpadFunctionsGroupVersion"].toString() + "-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.oracle.oci.sdk:oci-java-sdk-objectstorage-generated:3.67.0")
    implementation("com.oracle.oci.sdk:oci-java-sdk-common-httpclient-jersey:3.67.0")
    // gson will be supplied by the shared script
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
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree) // OR .map { zipTree(it) }
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    shouldRunAfter(tasks.build)
}