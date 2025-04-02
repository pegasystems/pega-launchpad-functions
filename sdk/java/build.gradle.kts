plugins {
    id("java")
}

group = "com.pega.launchpad.sdk"
version = extra["PegaLaunchpadFunctionsGroupVersion"].toString() + "-SNAPSHOT";

repositories {
    mavenCentral()
}

val junitVersion = extra["PegaLaunchpadFunctionsJunitVersion"].toString();

dependencies {
    compileOnly ("com.fasterxml.jackson.core:jackson-annotations:2.15.2")


    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation ("com.fasterxml.jackson.core:jackson-annotations:2.15.2")
    testImplementation("com.fasterxml.jackson.core:jackson-core:2.15.2")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}
