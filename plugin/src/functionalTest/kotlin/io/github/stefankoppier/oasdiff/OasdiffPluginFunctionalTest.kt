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
    @get:Rule val tempFolder = TemporaryFolder()

    private lateinit var runner: GradleRunner

    private val projectDir
        get() = tempFolder.root

    private val buildFile
        get() = projectDir.resolve("build.gradle")

    @BeforeTest
    fun before() {
        buildFile.writeText(
            """
                plugins {
                    id("io.github.stefankoppier.oasdiff")
                }
            """.trimIndent()
        )

        runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withProjectDir(projectDir)
    }

    @Test
    fun `can run oasdiffInstall task`() {
        runner.withArguments("oasdiffInstall")
        runner.build()

        projectDir.resolve(".gradle${separator}oasdiff").run {
            assertTrue("folder '$path' does not exist") { exists() }
            this.resolve("1.3.21").run {
                assertTrue("folder '$path' does not exist") { exists() }
                this.resolve("oasdiff.exe").run {
                    assertTrue("file '$path' does not exist") { exists() }
                }
            }
        }
    }

    @Test
    fun `can run oasDiffBreaking task`() {
        val base = projectDir.resolve("base-api.yml")
        File("./src/functionalTest/resources/base-api.yml").copyTo(base)
        val revision = projectDir.resolve("revision-api.yml")
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
