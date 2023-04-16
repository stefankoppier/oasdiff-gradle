# oasdiff-gradle
Gradle plugin for [oasdiff](https://github.com/Tufin/oasdiff): a tool to compare and detect breaking changes in OpenAPI specs.

## Usage
First, add the plugin to the plugin block of the build script
```kotlin
plugins {
    id("io.github.stefankoppier.oasdiff") version "x.y.z"
}
```
This will add two Gradle tasks: `oasdiffInstall` and `oasdiffCheckBreaking` in the group `oasdiff`.
`oasdiffInstall` is not required to be executed manually as `oasdiffCheckBreaking` depends on `oasdiffInstall`.
