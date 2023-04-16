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

### Minimal configuration
To run `oasdiffCheckBreaking` the following minimal configuration must be applied
```kotlin
oasdiff {
    base.set("base-specification.yml")
    revision.set("revision-specification.yml")
}
```
which sets the base specification and the revision to compare the breaking changes.

### Additional configuration
The following addition configuration options are available
| Name       | Description                                                   |
|------------|---------------------------------------------------------------|
| directory  | The directory in which to install the oasdiff executable.     |
| exclude    | The set of elements to exclude in the breaking changes check. |
| failOnDiff | Let the task fail when a breaking change has been detected.   |
| failOnWarn | Let the task fail when a warning has been detected.           |

# Credits
This project is a wrapper on [oasdiff](https://github.com/Tufin/oasdiff) by [Tufin](https://github.com/Tufin).