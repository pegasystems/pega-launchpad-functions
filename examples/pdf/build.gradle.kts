
plugins {
    id("java")
}

group = "com.pega.launchpad.pdf"
version = extra["PegaLaunchpadFunctionsGroupVersion"].toString() + "-SNAPSHOT";

repositories {
    mavenCentral()
}

val junitVersion = extra["PegaLaunchpadFunctionsJunitVersion"].toString();

dependencies {
    testImplementation(platform("org.junit:junit-bom:${junitVersion}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.google.code.gson:gson:2.12.1")
    implementation("org.apache.pdfbox:pdfbox:3.0.4")
    implementation("com.amazonaws:aws-lambda-java-core:1.2.3")
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