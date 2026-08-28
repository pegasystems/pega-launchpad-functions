plugins {
    id("base")
}

val exampleProjects = subprojects.filter { it.path.startsWith(":examples:") }

val collectExampleDistributions = tasks.register<Sync>("collectExampleDistributions") {
    group = "build"
    description = "Collect example JAR and ZIP distributions in the root build directory"

    exampleProjects.forEach { exampleProject ->
        dependsOn(exampleProject.tasks.named("build"))
        from(exampleProject.layout.buildDirectory.dir("libs")) {
            include("*.jar")
        }
        from(exampleProject.layout.buildDirectory.dir("distributions")) {
            include("*.zip")
        }
    }

    into(layout.buildDirectory.dir("distributions"))
}

tasks.named("build") {
    dependsOn(collectExampleDistributions)
}
