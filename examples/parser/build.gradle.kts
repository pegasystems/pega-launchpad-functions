
plugins {
    id("java")
}

group = "com.pega.launchpad.parser"
version = extra["PegaLaunchpadFunctionsGroupVersion"].toString() + "-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = extra["PegaLaunchpadFunctionsJunitVersion"].toString()

dependencies {
    testImplementation(platform("org.junit:junit-bom:${junitVersion}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.apache.commons:commons-csv:1.13.0")
    implementation("com.google.code.gson:gson:2.12.1")
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
    into("lib") {
        from(configurations.runtimeClasspath)
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    shouldRunAfter(tasks.build)
}