plugins {
    id("base")
}

version = extra["PegaLaunchpadFunctionsGroupVersion"].toString() + "-SNAPSHOT"

// Apply a common Python "build a deployable zip" convention to every subproject.
// Each subproject is expected to contain:
//   - src/                  Python source files
//   - requirements.txt      pip-installable dependencies (may be empty)
//   - resources/ (optional) Static resources to bundle alongside the source
//
// Each subproject produces its own <name>-<version>.zip under build/distributions,
// keeping the deployable artifacts small and focused on a single function.
subprojects {
    apply(plugin = "base")

    version = rootProject.extra["PegaLaunchpadFunctionsGroupVersion"].toString() + "-SNAPSHOT"

    val requirementsFile = file("requirements.txt")
    val depsDir = layout.buildDirectory.dir("python-deps")
    val resourcesDir = file("resources")

    val downloadPythonDependencies = tasks.register<Exec>("downloadPythonDependencies") {
        group = "build"
        description = "Download Python dependencies from requirements.txt"

        outputs.dir(depsDir)
        inputs.file(requirementsFile)

        commandLine(
            "pip",
            "install",
            "-r",
            requirementsFile.absolutePath,
            "-t",
            depsDir.get().asFile.absolutePath
        )

        doFirst {
            depsDir.get().asFile.mkdirs()
        }

        // Skip pip entirely when there are no actual dependencies declared.
        onlyIf {
            requirementsFile.exists() && requirementsFile.readLines().any {
                val trimmed = it.trim()
                trimmed.isNotEmpty() && !trimmed.startsWith("#")
            }
        }
    }

    val zipPythonExample = tasks.register<Zip>("zipPythonExample") {
        group = "build"
        description = "Create a deployable zip with Python source code, resources, and dependencies"

        dependsOn(downloadPythonDependencies)

        from("src") { into("") }

        if (resourcesDir.isDirectory) {
            from("resources") { into("") }
        }

        from(depsDir) { into("") }

        archiveFileName.set(project.name + "-" + project.version + ".zip")
        destinationDirectory.set(layout.buildDirectory.dir("distributions"))
    }

    tasks.named("build") {
        dependsOn(zipPythonExample)
    }
}
