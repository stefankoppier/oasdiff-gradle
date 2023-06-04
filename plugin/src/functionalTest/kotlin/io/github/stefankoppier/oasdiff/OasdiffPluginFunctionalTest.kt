package io.github.stefankoppier.oasdiff

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import java.io.File
import java.io.File.separator
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class OasdiffPluginFunctionalTest {

    @get:Rule
    val folder = TemporaryFolder()

    private lateinit var runner: GradleRunner

    private val buildFile get() = folder.root.resolve("build.gradle")

    @BeforeTest
    fun before() {
        runner = GradleRunner.create()
            .forwardOutput()
            .withPluginClasspath()
            .withProjectDir(folder.root)
            .withTestKitDir(folder.newFolder())
            .withJaCoCo()

        buildFile.writeText(
            """
                plugins {
                    id("io.github.stefankoppier.oasdiff")
                }
            """.trimIndent()
        )
    }

    @Test
    fun `can run oasdiffInstall task`() {
        runner.withArguments("oasdiffInstall")
        runner.build()

        runner.projectDir.resolve(".gradle${separator}oasdiff").run {
            assertTrue("folder '$path' does not exist") { exists() }
            this.resolve(VERSION).run {
                assertTrue("folder '$path' does not exist") { exists() }
                this.resolve("oasdiff.exe").run {
                    assertTrue("file '$path' does not exist") { exists() }
                }
            }
        }
    }

    @Test
    fun `can run oasDiffBreaking task`() {
        val base = runner.projectDir.resolve("base-api.yml")
        File("./src/functionalTest/resources/base-api.yml").copyTo(base)
        val revision = runner.projectDir.resolve("revision-api.yml")
        File("./src/functionalTest/resources/revision-api.yml").copyTo(revision)

        buildFile.appendText(
            """
                
                oasdiff {
                    base.set("${base.toPath().toString().replace("\\", "/")}")
                    revision.set("${revision.toPath().toString().replace("\\", "/")}")
                    failOnDiff.set(true)
                }
            """.trimIndent()
        )
        runner.withArguments("oasdiffCheckBreaking")
        runner.buildAndFail()
    }
}
