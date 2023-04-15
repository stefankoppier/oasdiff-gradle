rootProject.name = "oasdiff-gradle"

include("plugin")

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}