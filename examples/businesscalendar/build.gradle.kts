plugins {
    id("base")
}

version = extra["PegaLaunchpadFunctionsGroupVersion"].toString() + "-SNAPSHOT"

val dependencyDirectory = layout.buildDirectory.dir("dependencies")
val installBusinessCalendarDependencies = tasks.register<Exec>("installBusinessCalendarDependencies") {
    val packageFiles = files("src/package.json", "src/package-lock.json")
    inputs.files(packageFiles)
    outputs.dir(dependencyDirectory.map { it.dir("node_modules") })
    workingDir(dependencyDirectory)
    commandLine(
        if (System.getProperty("os.name").startsWith("Windows")) "npm.cmd" else "npm",
        "ci",
        "--omit=dev",
        "--ignore-scripts"
    )
    doFirst {
        delete(dependencyDirectory)
        copy {
            from(packageFiles)
            into(dependencyDirectory)
        }
    }
}

tasks.register<Zip>("businesscalendar") {
    dependsOn(installBusinessCalendarDependencies)
    from("./src") {
        exclude("node_modules/**")
    }
    from(dependencyDirectory.map { it.dir("node_modules") }) {
        into("node_modules")
        exclude(".bin/**", ".package-lock.json")
    }
    archiveFileName.set(project.name + "-" + project.version + ".zip")
    destinationDirectory.set(layout.buildDirectory.dir("distributions"))
}

tasks.named("build") {
    dependsOn("businesscalendar")
}

extra["resourceCenterArtifactTask"] = "businesscalendar"
extra["resourceCenterArtifactTarget"] = "calculateBusinessDays.zip"
apply(from = rootProject.file("gradle/resourcecenter-artifact.gradle.kts"))
