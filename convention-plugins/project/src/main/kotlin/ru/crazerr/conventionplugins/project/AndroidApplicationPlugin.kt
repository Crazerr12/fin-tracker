package ru.crazerr.conventionplugins.project

import com.android.build.api.dsl.ApplicationDefaultConfig
import ru.crazerr.conventionplugins.base.androidConfig
import ru.crazerr.conventionplugins.base.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugins.android.application.get().pluginId)
                apply(libs.plugins.kotlin.compose.get().pluginId)
                apply("android.compose.config")
            }

            androidConfig {
                defaultConfig {
                    this as ApplicationDefaultConfig

                    targetSdk = libs.versions.targetSdk.get().toInt()

                    versionCode = libs.versions.appVersionCode.get().toInt()
                    versionName = libs.versions.appVersionName.get()
                }
            }
        }
    }
}