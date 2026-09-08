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
    implementation(platform("software.amazon.awssdk:bom:2.54.13"))
    implementation(platform("io.netty:netty-bom:4.2.17.Final"))
    implementation("org.apache.httpcomponents.client5:httpclient5:5.6.4")
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

extra["resourceCenterArtifactTask"] = "jar"
extra["resourceCenterArtifactTarget"] = "aws.jar"
apply(from = rootProject.file("gradle/resourcecenter-artifact.gradle.kts"))