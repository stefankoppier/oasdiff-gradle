import java.io.ByteArrayOutputStream

plugins {
    id("java-gradle-plugin")
    id("maven-publish")
    id("jacoco")
    id("jvm-test-suite")
    alias(libs.plugins.kotlin)
    alias(libs.plugins.gradle.publish)
    alias(libs.plugins.sonarqube)
    alias(libs.plugins.ktlint)
    id("pl.droidsonroids.jacoco.testkit") version "1.0.12"
}

repositories {
    mavenCentral()
}

group = "io.github.stefankoppier"
version = "0.0.2"

dependencies {
    implementation(libs.kotlin.stdlib.jdk8)
}

testing {
    suites {
        configureEach {
            if (this is JvmTestSuite) {
                useJUnit()

                dependencies {
                    implementation(libs.kotlin.test)
                    implementation(libs.kotlin.test.junit)
                }
            }
        }

        val test by getting(JvmTestSuite::class) {
            targets {
                all {
                    testTask.configure {
                        finalizedBy("functionalTest")
                    }
                }
            }
        }

        register<JvmTestSuite>("functionalTest") {
            dependencies {
                implementation(project(":plugin"))
                implementation(gradleTestKit())
            }

            targets {
                all {
                    testTask.configure {
                        dependsOn(tasks.named("generateJacocoTestKitProperties"))
                        finalizedBy("jacocoTestReport")
                    }
                }
            }
        }
    }
}

gradlePlugin {
    website.set("https://github.com/stefankoppier/oasdiff-gradle")
    vcsUrl.set("https://github.com/stefankoppier/oasdiff-gradle")
    testSourceSets(sourceSets.findByName("functionalTest"))

    plugins {
        create("oasdiff") {
            id = "io.github.stefankoppier.oasdiff"
            displayName = "oasdiff"
            description = "Wrapper for the Tufin oasdiff OpenAPI breaking changes detection tool."
            tags.set(listOf("oasdiff", "OpenAPI", "breaking changes", "api validation"))
            implementationClass = "io.github.stefankoppier.oasdiff.OasdiffPlugin"
        }
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
    }
}

tasks.named("assemble") {
    dependsOn("ktlintFormat")
}

ktlint {
    disabledRules.set(setOf("no-wildcard-imports"))
}

sonar {
    properties {
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.organization", "stefankoppier")
        property("sonar.projectKey", "stefankoppier_oasdiff-gradle")
        property("sonar.branch", branch())
    }
}

fun branch(): String {
    return try {
        val byteOut = ByteArrayOutputStream()
        project.exec {
            commandLine = "git rev-parse --abbrev-ref HEAD".split(" ")
            standardOutput = byteOut
        }
        String(byteOut.toByteArray()).trim().also {
            if (it == "HEAD") {
                logger.warn("Unable to determine current branch: Project is checked out with detached head!")
            }
        }
    } catch (e: Exception) {
        logger.warn("Unable to determine current branch: ${e.message}")
        "Unknown Branch"
    }
}
