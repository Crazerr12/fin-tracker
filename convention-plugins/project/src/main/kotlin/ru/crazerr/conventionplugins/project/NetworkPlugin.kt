package ru.crazerr.conventionplugins.project

import org.gradle.api.Plugin
import org.gradle.api.Project
import ru.crazerr.conventionplugins.base.implementation
import ru.crazerr.conventionplugins.base.libs

class NetworkPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugins.jetbrains.kotlin.jvm.get().pluginId)
                apply("serialization.plugin")
                apply("kotlin.base.config")
            }

            with(dependencies) {
                // Ktor
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization)

                // Koin
                implementation(platform(libs.koin.bom))
                implementation(libs.koin.core)
            }
        }
    }
}