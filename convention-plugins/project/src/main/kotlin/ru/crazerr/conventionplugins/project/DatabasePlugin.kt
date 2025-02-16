package ru.crazerr.conventionplugins.project

import org.gradle.api.Plugin
import org.gradle.api.Project
import ru.crazerr.conventionplugins.base.implementation
import ru.crazerr.conventionplugins.base.libs

class DatabasePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugins.kotlin.android.get().pluginId)
                apply(libs.plugins.kotlin.compose.get().pluginId)
                apply("android.library.plugin")
                apply(libs.plugins.ksp.get().pluginId)
                apply("serialization.plugin")
            }

            with(dependencies) {
                // Room
                add("ksp", libs.room.compiler)
                implementation(libs.room.runtime)
                implementation(libs.room.ktx)

                // Android
                implementation(libs.androidx.core.ktx)

                // Koin
                implementation(platform(libs.koin.bom))
                implementation(libs.koin.core)
            }
        }
    }
}