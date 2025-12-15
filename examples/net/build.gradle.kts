plugins {
    id("java")
}

apply(from = rootProject.file("gradle/common-dependencies.gradle.kts"))

group = "com.pega.launchpad.net"
version = extra["PegaLaunchpadFunctionsGroupVersion"].toString() + "-SNAPSHOT"


repositories {
    mavenCentral()
}

val junitVersion = extra["PegaLaunchpadFunctionsJunitVersion"].toString()

dependencies {
    implementation("org.apache.httpcomponents.client5:httpclient5:5.4.4")
    implementation("com.google.code.gson:gson:2.12.1")
}

// call shared helpers
applyCommonTestLibraries()
applyAnnotations()
applyGson()

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