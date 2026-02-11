plugins {
    id("base")
}

version = extra["PegaLaunchpadFunctionsGroupVersion"].toString() + "-SNAPSHOT"

// Task to download Python dependencies
tasks.register<Exec>("downloadPythonDependencies") {
    group = "build"
    description = "Download Python dependencies from requirements.txt"

    val requirementsFile = file("requirements.txt")
    val depsDir = layout.buildDirectory.dir("python-deps").get().asFile

    outputs.dir(depsDir)
    inputs.file(requirementsFile)

    commandLine("pip", "install", "-r", requirementsFile.absolutePath, "-t", depsDir.absolutePath)

    doLast {
        if (!depsDir.exists()) {
            depsDir.mkdirs()
        }
    }
}

tasks.register<Zip>("zipPythonExamples") {
    group = "build"
    description = "Create zip file with Python source code and dependencies"

    dependsOn("downloadPythonDependencies")

    // Include source files
    from("./src") {
        into("")
    }

    // Include resources
    from("./resources") {
        into("")
    }

    // Include Python dependencies (site-packages)
    from(layout.buildDirectory.dir("python-deps")) {
        into("")
    }

    archiveFileName.set(project.name + "-" + project.version + ".zip")
    destinationDirectory.set(layout.buildDirectory.dir("distributions"))
}

tasks.named("build") {
    dependsOn("zipPythonExamples")
}