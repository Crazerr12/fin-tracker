package ru.crazerr.conventionplugins.project

import org.gradle.api.Plugin
import org.gradle.api.Project
import ru.crazerr.conventionplugins.base.implementation
import ru.crazerr.conventionplugins.base.libs

class CoilPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(dependencies) {
                implementation(platform(libs.coil.bom))
                implementation(libs.coil.compose)
                implementation(libs.coil.network.ktor3)
            }
        }
    }
}