plugins {
    id("java")
}

// set flags for shared dependencies
extra["useCommonTestLibraries"] = true
extra["useAnnotations"] = true
extra["useGson"] = true

apply(from = rootProject.file("gradle/common-dependencies.gradle.kts"))

group = "com.pega.launchpad.aws"
version = extra["PegaLaunchpadFunctionsGroupVersion"].toString() + "-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = extra["PegaLaunchpadFunctionsJunitVersion"].toString()

dependencies {
    // module-specific AWS deps
    implementation(platform("software.amazon.awssdk:bom:2.28.24"))
    implementation("software.amazon.awssdk:s3")
    implementation("software.amazon.awssdk:comprehend")
    implementation("software.amazon.awssdk:translate")
    implementation("software.amazon.awssdk:kms")
}


tasks.test {
    useJUnitPlatform()
}

// Ensure module compiles and tests with Java 11 (java.net.http etc.)
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
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