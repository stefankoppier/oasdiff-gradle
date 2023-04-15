package io.github.stefankoppier.oasdiff

import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

abstract class OasdiffPluginExtension {

    abstract val directory: Property<String>

    abstract val base: Property<String>

    abstract val revision: Property<String>

    abstract val exclude: SetProperty<OasdiffExclusion>

    abstract val failOnDiff: Property<Boolean>

    abstract val failOnWarn: Property<Boolean>
}

enum class OasdiffExclusion {
    EXAMPLES,
    DESCRIPTION,
    TITLE,
    SUMMARY,
    ENDPOINTS;

    override fun toString(): String {
        return when (this) {
            EXAMPLES -> "changes"
            DESCRIPTION -> "description"
            TITLE -> "title"
            SUMMARY -> "summary"
            ENDPOINTS -> "endpoints"
        }
    }
}
