package io.github.stefankoppier.oasdiff

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.File.separator

abstract class OasdiffCheckBreakingTask : DefaultTask() {

    @get:Internal
    abstract val directory: Property<String>

    @get:Input
    abstract val base: Property<String>

    @get:Input
    abstract val revision: Property<String>

    @get:Input
    abstract val exclusions: SetProperty<OasdiffExclusion>

    @get:Input
    abstract val failOnDiff: Property<Boolean>

    @get:Input
    abstract val failOnWarn: Property<Boolean>

    @TaskAction
    fun execute() {
        println(oasdiff())
    }

    fun oasdiff(): String {
        val stdout = ByteArrayOutputStream()
        project.exec {
            it.standardOutput = stdout
            it.workingDir = File(directory.get())
            it.executable = "${directory.get()}${separator}oasdiff.exe"

            it.args = arguments {
                argument("-check-breaking")
                argument("-composed")
                argument("-format", "yaml")
                argument("-base", base.get())
                argument("-revision", revision.get())
                argument("-exclude-elements", exclusions.get().joinToString(",")) { exclusions.get().isNotEmpty() }
                argument("-fail-on-diff") { failOnDiff.get() }
                argument("-fail-on-warns") { failOnWarn.get() }
            }
        }
        return stdout.toString()
    }

    private fun <String> arguments(init: MutableList<String>.() -> List<String>): List<String> {
        return mutableListOf<String>().init()
    }

    private fun MutableList<String>.argument(name: String, value: String? = null, predicate: () -> Boolean = { true }): MutableList<String> {
        if (predicate()) {
            this.add(name)
            value?.let { add(it) }
        }
        return this
    }

    private fun MutableList<String>.argument(name: String, predicate: () -> Boolean = { true }): MutableList<String> {
        if (predicate()) {
            this.add(name)
        }
        return this
    }
}