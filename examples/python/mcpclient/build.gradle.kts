plugins {
    id("base")
}

tasks.named<Exec>("downloadPythonDependencies") {
    val requirementsFile = file("requirements.txt")
    val dependenciesDirectory = layout.buildDirectory.dir("python-deps")

    if (System.getProperty("os.name").lowercase().contains("windows")) {
        fun String.toWslPath(): String =
            "/mnt/${substring(0, 1).lowercase()}${substring(2).replace('\\', '/')}"

        val wslRequirements = requirementsFile.absolutePath.toWslPath()
        val wslDependencies = dependenciesDirectory.get().asFile.absolutePath.toWslPath()
        val installCommand = listOf(
            "python3", "-m", "pip", "install", "--break-system-packages",
            "--requirement", "'$wslRequirements'",
            "--target", "'$wslDependencies'",
            "--upgrade",
            "--platform", "manylinux2014_x86_64",
            "--implementation", "cp",
            "--python-version", "3.12",
            "--only-binary=:all:"
        ).joinToString(" ")

        commandLine("wsl.exe", "bash", "-lc", installCommand)
    } else {
        commandLine(
            "python", "-m", "pip", "install",
            "--requirement", requirementsFile.absolutePath,
            "--target", dependenciesDirectory.get().asFile.absolutePath,
            "--upgrade",
            "--platform", "manylinux2014_x86_64",
            "--implementation", "cp",
            "--python-version", "3.12",
            "--only-binary=:all:"
        )
    }
}