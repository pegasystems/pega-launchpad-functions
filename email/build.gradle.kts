
plugins {
    id("java")
}

group = "com.pega.launchpad.email"
version = "0.1.6-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("jakarta.activation:jakarta.activation-api:2.1.3")
    implementation("jakarta.mail:jakarta.mail-api:2.1.3")
    implementation("com.sun.mail:jakarta.mail:2.0.1")
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