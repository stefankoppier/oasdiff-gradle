package io.github.stefankoppier.oasdiff

import org.gradle.testkit.runner.GradleRunner
import java.io.File

internal fun GradleRunner.withJaCoCo(): GradleRunner {
    File("./build/testkit/test/testkit-gradle.properties")
        .copyTo(File(projectDir, "gradle.properties"))
    return this
}
