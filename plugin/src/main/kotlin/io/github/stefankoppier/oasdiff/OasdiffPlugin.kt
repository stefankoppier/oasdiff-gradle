package io.github.stefankoppier.oasdiff

import org.gradle.api.Project
import org.gradle.api.Plugin

class OasdiffPlugin: Plugin<Project> {

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
            it.workingDirectory.set(directory(extension))
            it.base.set(extension.base)
            it.revision.set(extension.revision)
            it.dependsOn(install)
        }
    }

    private fun directory(extension: OasdiffPluginExtension)
        = extension.directory.convention("${project.rootDir}/.gradle/oasdiff")

    companion object {
        const val version = "1.3.21"
        private const val OASDIFF = "oasdiff"
        private const val INSTALL_TASK_NAME = "oasdiffInstall"
        private const val BREAKING_CHANGE_TASK_NAME = "oasdiffCheckBreaking"
    }
}
