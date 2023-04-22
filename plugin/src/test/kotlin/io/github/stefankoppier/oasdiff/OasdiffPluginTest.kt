package io.github.stefankoppier.oasdiff

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import java.io.File.separator
import kotlin.test.*

class OasdiffPluginTest {

    private lateinit var project: Project

    @BeforeTest
    fun before() {
        project = ProjectBuilder.builder().build()
        project.plugins.apply("io.github.stefankoppier.oasdiff")
    }

    @Test
    fun `plugin registers oasdiffInstall task`() {
        val task = project.tasks.findByName("oasdiffInstall")

        assertNotNull(task)
        assertIs<OasdiffInstallTask>(task)
        task.run {
            assertEquals("oasdiff", group)
        }
    }

    @Test
    fun `plugin registers oasdiffCheckBreaking task`() {
        val task = project.tasks.findByName("oasdiffCheckBreaking")

        assertNotNull(task)
        assertIs<OasdiffCheckBreakingTask>(task)
        task.run {
            assertEquals("oasdiff", group)
            assertEquals("${project.rootDir}/.gradle/oasdiff${separator}$VERSION", directory.get())
            assertFalse { failOnDiff.get() }
            assertFalse { failOnWarn.get() }
        }
    }
}
