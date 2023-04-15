plugins {
    `java-gradle-plugin`
    `maven-publish`

    id("org.jetbrains.kotlin.jvm") version "1.8.20"
    id("org.jlleitschuh.gradle.ktlint") version "11.3.1"
}

repositories {
    mavenCentral()
}

group = "io.github.stefankoppier"
version = "0.0.1"

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
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
