plugins {
    id("base")
}

version = extra["PegaLaunchpadFunctionsGroupVersion"].toString() + "-SNAPSHOT"

tasks.register<Zip>("businesscalendar") {
    from("./src")
    archiveFileName.set(project.name + "-" + project.version + ".zip")
    destinationDirectory.set(layout.buildDirectory.dir("distributions"))
}

tasks.named("build") {
    dependsOn("businesscalendar")
}
