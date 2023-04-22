package io.github.stefankoppier.oasdiff

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileTree
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels

// TODO: only works on Windows?
abstract class OasdiffInstallTask : DefaultTask() {

    private val url = URL("https://github.com/Tufin/oasdiff/releases/download/v$VERSION/oasdiff_${VERSION}_windows_amd64.tar.gz")

    @get:Input
    abstract val workingDirectory: Property<String>

    @get:Internal
    val downloadDirectory: Provider<String> = workingDirectory.map { "${it}${separator}tmp$separator" }

    @TaskAction
    fun execute() {
        extract(download())
        cleanup()
    }

    private fun download(): FileTree {
        val path = "${downloadDirectory.get()}${separator}oasdiff.tar.gz"
        url.openStream().use {
            Channels.newChannel(it).use { rbc ->
                File(downloadDirectory.get()).mkdirs()
                FileOutputStream(path).use { file ->
                    file.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
                }
            }
        }
        return project.tarTree(path)
    }

    private fun extract(files: FileTree) {
        files.forEach { file ->
            val target = "${workingDirectory.map { "${it}${separator}$VERSION" }.get()}${separator}${file.name}"
            file.copyTo(File(target), true)
        }
    }

    private fun cleanup() {
        File(downloadDirectory.get()).deleteRecursively()
    }
}
