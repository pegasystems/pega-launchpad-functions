plugins {
    id("base")
}

tasks.register<Zip>("zipPythonExamples") {
    from("./src")
    archiveFileName.set("python.examples.zip")
    destinationDirectory.set(layout.buildDirectory.dir("distributions"))
}

tasks.named("build") {
    dependsOn("zipPythonExamples")
}