plugins {
    id("base")
}

tasks.register<Zip>("zipNodeJsExamples") {
    from(".")
    archiveFileName.set("nodejs.examples.zip")
    destinationDirectory.set(layout.buildDirectory.dir("distributions"))
}

tasks.named("build") {
    dependsOn("zipNodeJsExamples")
}