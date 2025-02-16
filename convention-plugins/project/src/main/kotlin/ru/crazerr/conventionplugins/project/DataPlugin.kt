package ru.crazerr.conventionplugins.project

import org.gradle.api.Plugin
import org.gradle.api.Project
import ru.crazerr.conventionplugins.base.implementation
import ru.crazerr.conventionplugins.base.libs

class DataPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("android.library.plugin")
                apply("serialization.plugin")
            }

            with(dependencies) {
                // Koin
                implementation(platform(libs.koin.bom))
                implementation(libs.koin.core)

                // Ktor
                implementation(libs.ktor.client.core)

                // Network
                implementation(project(":core:network"))

                // Database
                implementation(project(":core:database"))
            }
        }
    }
}