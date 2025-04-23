package ru.crazerr.conventionplugins.project

import org.gradle.api.Plugin
import org.gradle.api.Project
import ru.crazerr.conventionplugins.base.implementation
import ru.crazerr.conventionplugins.base.libs

class UtilsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugins.kotlin.android.get().pluginId)
                apply(libs.plugins.kotlin.compose.get().pluginId)
                apply("android.library.plugin")
                apply("serialization.plugin")
                apply("coil.plugin")
            }

            with(dependencies) {
                // Android
                implementation(libs.androidx.core.ktx)
                implementation(libs.androidx.appcompat)
                implementation(libs.material)
                implementation(platform(libs.androidx.compose.bom))
                implementation(libs.androidx.ui)
                implementation(libs.androidx.ui.graphics)
                implementation(libs.androidx.ui.tooling.preview)
                implementation(libs.androidx.material3)

                // Decompose
                implementation(libs.decompose)
                implementation(libs.decompose.extenstions.compose)

                // Koin
                implementation(platform(libs.koin.bom))
                implementation(libs.koin.compose)
                implementation(libs.koin.core)

                // Paging
                implementation(libs.paging.runtime)
                implementation(libs.paging.compose)
            }
        }
    }
}