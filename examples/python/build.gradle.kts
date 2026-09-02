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
    val sourceWheelDir = layout.buildDirectory.dir("python-wheels")
    val resourcesDir = file("resources")
    val needsSourceWheel = requirementsFile.exists() && requirementsFile.readLines().any {
        it.substringBefore("#").trim().startsWith("extract-msg")
    }

    fun String.toWslPath(): String =
        "/mnt/${substring(0, 1).lowercase()}${substring(2).replace('\\', '/')}"

    val preparePythonSourceDependencies = tasks.register<Exec>("preparePythonSourceDependencies") {
        group = "build"
        description = "Build source-only Python dependencies as wheels"

        outputs.dir(sourceWheelDir)

        onlyIf { needsSourceWheel }

        doFirst {
            sourceWheelDir.get().asFile.mkdirs()
        }

        if (System.getProperty("os.name").lowercase().contains("windows")) {
            val wslWheelDirectory = sourceWheelDir.get().asFile.absolutePath.toWslPath()
            commandLine(
                "wsl.exe", "bash", "-lc",
                "python3 -m pip wheel --no-deps --no-binary=:all: " +
                    "--wheel-dir '$wslWheelDirectory' red-black-tree-mod"
            )
        } else {
            commandLine(
                "python", "-m", "pip", "wheel",
                "--no-deps", "--no-binary=:all:",
                "--wheel-dir", sourceWheelDir.get().asFile.absolutePath,
                "red-black-tree-mod"
            )
        }
    }

    val downloadPythonDependencies = tasks.register<Exec>("downloadPythonDependencies") {
        group = "build"
        description = "Download Python dependencies from requirements.txt"

        outputs.dir(depsDir)
        inputs.file(requirementsFile)

        dependsOn(preparePythonSourceDependencies)

        if (System.getProperty("os.name").lowercase().contains("windows")) {
            val wslRequirements = requirementsFile.absolutePath.toWslPath()
            val wslDependencies = depsDir.get().asFile.absolutePath.toWslPath()
            val wslWheelDirectory = sourceWheelDir.get().asFile.absolutePath.toWslPath()
            val findLinks = if (needsSourceWheel) {
                listOf("--find-links", "'$wslWheelDirectory'")
            } else {
                emptyList()
            }
            val installCommand = listOf(
                "python3", "-m", "pip", "install",
                "--break-system-packages",
                "--requirement", "'$wslRequirements'",
                "--target", "'$wslDependencies'",
                "--upgrade",
                "--platform", "manylinux2014_x86_64",
                "--implementation", "cp",
                "--python-version", "3.12",
                "--only-binary=:all:"
            ) + findLinks

            commandLine("wsl.exe", "bash", "-lc", installCommand.joinToString(" "))
        } else {
            val findLinks = if (needsSourceWheel) {
                listOf("--find-links", sourceWheelDir.get().asFile.absolutePath)
            } else {
                emptyList()
            }
            commandLine(*(listOf(
                "python", "-m", "pip", "install",
                "--requirement", requirementsFile.absolutePath,
                "--target", depsDir.get().asFile.absolutePath,
                "--upgrade",
                "--platform", "manylinux2014_x86_64",
                "--implementation", "cp",
                "--python-version", "3.12",
                "--only-binary=:all:"
            ) + findLinks).toTypedArray())
        }

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
