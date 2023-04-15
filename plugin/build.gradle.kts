plugins {
    id("java-gradle-plugin")
    id("maven-publish")
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ktlint)
}

repositories {
    mavenCentral()
}

group = "io.github.stefankoppier"
version = "0.0.1"

dependencies {
    implementation(platform(libs.kotlin.bom))
    implementation(libs.kotlin.stdlib.jdk8)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit)
}

gradlePlugin {
    plugins {
        create("oasdiff") {
            id = "io.github.stefankoppier.oasdiff"
            implementationClass = "io.github.stefankoppier.oasdiff.OasdiffPlugin"
        }
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}

val functionalTestSourceSet = sourceSets.create("functionalTest") {
}

configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])

val functionalTest by tasks.registering(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
}

gradlePlugin.testSourceSets(functionalTestSourceSet)

tasks.named<Task>("check") {
    dependsOn(functionalTest)
}

tasks.named("assemble") {
    dependsOn("ktlintFormat")
}

ktlint {
    disabledRules.set(setOf("no-wildcard-imports"))
}
