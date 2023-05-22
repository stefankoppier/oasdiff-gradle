plugins {
    id("java-gradle-plugin")
    id("maven-publish")
    id("jacoco")
    alias(libs.plugins.kotlin)
    alias(libs.plugins.gradle.publish)
    alias(libs.plugins.sonarqube)
    alias(libs.plugins.ktlint)
}

repositories {
    mavenCentral()
}

group = "io.github.stefankoppier"
version = "0.0.2"

dependencies {
    implementation(platform(libs.kotlin.bom))
    implementation(libs.kotlin.stdlib.jdk8)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit)
}

gradlePlugin {
    website.set("https://github.com/stefankoppier/oasdiff-gradle")
    vcsUrl.set("https://github.com/stefankoppier/oasdiff-gradle")

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

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}
tasks.jacocoTestReport {
    dependsOn(tasks.test)
}

val functionalTestSourceSet = sourceSets.create("functionalTest") {
}

configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])

val functionalTest by tasks.registering(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
}

tasks.named("compileFunctionalTestKotlin") {
    dependsOn += tasks.compileKotlin
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

sonar {
    properties {
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.organization", "stefankoppier")
        property("sonar.projectKey", "oasdiff-gradle")
    }
}
