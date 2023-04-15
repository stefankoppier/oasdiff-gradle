package io.github.stefankoppier.oasdiff

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

class OasdiffPlugin : Plugin<Project> {

    private lateinit var project: Project

    override fun apply(project: Project) {
        this.project = project

        val extension = project.extensions
            .create(OASDIFF, OasdiffPluginExtension::class.java)

        val install = project.tasks.register(INSTALL_TASK_NAME, OasdiffInstallTask::class.java) {
            it.group = OASDIFF
            it.workingDirectory.set(directory(extension))
        }
        project.tasks.register(BREAKING_CHANGE_TASK_NAME, OasdiffCheckBreakingTask::class.java) {
            it.group = OASDIFF
            it.directory.set(directory(extension).map { dir -> "${dir}${File.separator}$version" })
            it.base.set(extension.base)
            it.revision.set(extension.revision)
            it.exclusions.set(extension.exclude)
            it.failOnDiff.set(failOnDiff(extension))
            it.failOnWarn.set(failOnWarn(extension))
            it.dependsOn(install)
        }
    }

    private fun directory(extension: OasdiffPluginExtension) =
        extension.directory.convention("${project.rootDir}/.gradle/oasdiff")

    private fun failOnDiff(extension: OasdiffPluginExtension) =
        extension.failOnDiff.convention(false)

    private fun failOnWarn(extension: OasdiffPluginExtension) =
        extension.failOnWarn.convention(false)

    companion object {
        const val version = "1.3.21"
        private const val OASDIFF = "oasdiff"
        private const val INSTALL_TASK_NAME = "oasdiffInstall"
        private const val BREAKING_CHANGE_TASK_NAME = "oasdiffCheckBreaking"
    }
}
