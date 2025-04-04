plugins {
    id("base")
}

tasks.register<Zip>("zipNodeJsExamples") {
    from("./src")
    archiveFileName.set("nodejs.examples.zip")
    destinationDirectory.set(layout.buildDirectory.dir("distributions"))
}

tasks.named("build") {
    dependsOn("zipNodeJsExamples")
}