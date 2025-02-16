package ru.crazerr.conventionplugins.project

import com.android.tools.r8.internal.ui
import org.gradle.api.Plugin
import org.gradle.api.Project
import ru.crazerr.conventionplugins.base.implementation
import ru.crazerr.conventionplugins.base.libs
import kotlin.jvm.kotlin

class DomainPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugins.jetbrains.kotlin.jvm.get().pluginId)
                apply("kotlin.base.config")
            }

            with(dependencies) {
                // Koin
                implementation(platform(libs.koin.bom))
                implementation(libs.koin.core)
            }
        }
    }
}