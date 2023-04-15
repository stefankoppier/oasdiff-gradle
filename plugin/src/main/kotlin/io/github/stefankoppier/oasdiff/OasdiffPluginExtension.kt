package io.github.stefankoppier.oasdiff

import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

abstract class OasdiffPluginExtension {

    abstract val directory: Property<String>

    abstract val base: Property<String>

    abstract val revision: Property<String>

    abstract val exclude: SetProperty<OasdiffExclusion>
}

enum class OasdiffExclusion {
    EXAMPLES,
    DESCRIPTION,
    TITLE,
    SUMMARY,
    ENDPOINTS,
}