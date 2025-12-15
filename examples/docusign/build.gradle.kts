plugins {
    id("java")
}

// set flags for shared dependencies
extra["useCommonTestLibraries"] = true
extra["useAnnotations"] = true
extra["useJacksonImplementation"] = true

apply(from = rootProject.file("gradle/common-dependencies.gradle.kts"))

group = "com.pega.launchpad.docusign"
version = extra["PegaLaunchpadFunctionsGroupVersion"].toString() + "-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = extra["PegaLaunchpadFunctionsJunitVersion"].toString()

dependencies {
    // module-specific dependencies
    implementation("com.docusign:docusign-esign-java:6.4.0")
    // jackson-databind will be provided by shared script via useJacksonImplementation flag
    implementation("jakarta.ws.rs:jakarta.ws.rs-api:3.1.0")
    implementation("org.glassfish.jersey.media:jersey-media-multipart:3.1.10")
    implementation("org.glassfish.jersey.core:jersey-client:3.1.10")
    implementation("org.glassfish.jersey.media:jersey-media-json-jackson:3.1.10")
    implementation("com.auth0:java-jwt:4.4.0")
    implementation("org.bouncycastle:bcprov-jdk15to18:1.80")
}

// shared deps are applied via flags above

// Ensure module compiles with Java 11 so APIs like List.of are available
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