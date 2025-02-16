package ru.crazerr.conventionplugins.project

import org.gradle.api.Plugin
import org.gradle.api.Project
import ru.crazerr.conventionplugins.base.implementation
import ru.crazerr.conventionplugins.base.libs

class SerializationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugins.serialization.get().pluginId)
            }

            with(dependencies) {
                implementation(libs.serialization.json)
            }
        }
    }
}