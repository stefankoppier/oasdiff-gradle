package io.github.stefankoppier.oasdiff

import io.github.stefankoppier.oasdiff.OasdiffPlugin.Companion.version
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.File.separator

abstract class OasdiffCheckBreakingTask : DefaultTask() {

    // TODO: duplicate variables with OasdiffInstallTask
    @get:Input
    abstract val workingDirectory: Property<String>

    @get:Internal
    val target: Provider<String> = workingDirectory.map { "${it}${separator}${version}" }

    @get:InputFile
    abstract val base: Property<String>

    @get:InputFile
    abstract val revision: Property<String>

    @TaskAction
    fun execute() {
        println(oasdiff())
    }

    fun oasdiff(): String {
        val stdout = ByteArrayOutputStream()
        project.exec {
            it.standardOutput = stdout
            it.workingDir = File(target.get())
            it.executable = "${target.get()}${separator}oasdiff.exe"

            it.args = listOf(
                "-check-breaking",
                "-composed",
                "-format", "yaml",
                "-base", base.get(),
                "-revision", revision.get(),
            )
        }
        return stdout.toString()
    }
}