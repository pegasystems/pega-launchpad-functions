
plugins {
    id("java")
}

group = "com.pega.launchpad.docusign"
version = extra["PegaLaunchpadFunctionsGroupVersion"].toString() + "-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = extra["PegaLaunchpadFunctionsJunitVersion"].toString()

dependencies {
    testImplementation(platform("org.junit:junit-bom:${junitVersion}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.docusign:docusign-esign-java:6.1.0-RC1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.19.0")
    implementation("jakarta.ws.rs:jakarta.ws.rs-api:3.1.0")
    implementation("org.glassfish.jersey.media:jersey-media-multipart:3.1.10")
    implementation("org.glassfish.jersey.core:jersey-client:3.1.10")
    implementation("org.glassfish.jersey.media:jersey-media-json-jackson:3.1.10")
    implementation("com.auth0:java-jwt:3.19.4")
    implementation("org.bouncycastle:bcprov-jdk15to18:1.80")
    compileOnly("org.jetbrains:annotations:24.1.0")
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
