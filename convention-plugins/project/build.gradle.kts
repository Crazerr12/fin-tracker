import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "ru.crazerr.conventionplugins"

dependencies {
    implementation(libs.gradleplugin.android)
    implementation(libs.gradleplugin.kotlin)
    implementation(libs.gradleplugin.compose)
    implementation(libs.gradleplugin.composeCompiler)
    // Workaround for version catalog working inside precompiled scripts
    // Issue - https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.gradleplugin.base)
}

private val projectJavaVersion: JavaVersion = JavaVersion.toVersion(libs.versions.java.get())

java {
    sourceCompatibility = projectJavaVersion
    targetCompatibility = projectJavaVersion
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.fromTarget(projectJavaVersion.toString()))
}

gradlePlugin {
    plugins {
        register("android.application.plugin") {
            id  = "android.application.plugin"
            implementationClass = "ru.crazerr.conventionplugins.project.AndroidApplicationPlugin"
        }

        register("android.library.plugin") {
            id = "android.library.plugin"
            implementationClass = "ru.crazerr.conventionplugins.project.AndroidLibraryPlugin"
        }

        register("presentation.plugin") {
            id = "presentation.plugin"
            implementationClass = "ru.crazerr.conventionplugins.project.PresentationPlugin"
        }

        register("domain.plugin") {
            id = "domain.plugin"
            implementationClass = "ru.crazerr.conventionplugins.project.DomainPlugin"
        }

        register("data.plugin") {
            id = "data.plugin"
            implementationClass = "ru.crazerr.conventionplugins.project.DataPlugin"
        }

        register("network.plugin") {
            id = "network.plugin"
            implementationClass = "ru.crazerr.conventionplugins.project.NetworkPlugin"
        }

        register("database.plugin") {
            id = "database.plugin"
            implementationClass = "ru.crazerr.conventionplugins.project.DatabasePlugin"
        }

        register("serialization.plugin") {
            id = "serialization.plugin"
            implementationClass = "ru.crazerr.conventionplugins.project.SerializationPlugin"
        }

        register("coil.plugin") {
            id = "coil.plugin"
            implementationClass = "ru.crazerr.conventionplugins.project.CoilPlugin"
        }

        register("root.plugin") {
            id = "root.plugin"
            implementationClass = "ru.crazerr.conventionplugins.project.RootPlugin"
        }

        register("utils.plugin") {
            id = "utils.plugin"
            implementationClass = "ru.crazerr.conventionplugins.project.UtilsPlugin"
        }
    }
}