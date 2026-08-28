import java.nio.file.Files
import java.nio.file.StandardCopyOption
import org.gradle.api.tasks.bundling.AbstractArchiveTask

val artifactTaskName = extra["resourceCenterArtifactTask"].toString()
val artifactTargetName = extra["resourceCenterArtifactTarget"].toString()

require(artifactTaskName.isNotBlank()) {
    "resourceCenterArtifactTask must name an archive-producing task"
}
require(artifactTargetName.matches(Regex("[^/\\\\]+\\.(jar|zip)"))) {
    "resourceCenterArtifactTarget must be a JAR or ZIP filename"
}

val artifactTask = tasks.named<AbstractArchiveTask>(artifactTaskName) {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}
val resourceCenterArtifact = rootProject.layout.projectDirectory.file(
    "resourcecenter/$artifactTargetName"
)

val syncResourceCenterArtifact = tasks.register("syncResourceCenterArtifact") {
    group = "build"
    description = "Copies the built archive to resourcecenter/$artifactTargetName"
    dependsOn(artifactTask)
    inputs.file(artifactTask.flatMap { it.archiveFile })
    outputs.file(resourceCenterArtifact)

    doLast {
        Files.copy(
            artifactTask.get().archiveFile.get().asFile.toPath(),
            resourceCenterArtifact.asFile.toPath(),
            StandardCopyOption.REPLACE_EXISTING
        )
    }
}

tasks.named("build") {
    dependsOn(syncResourceCenterArtifact)
}