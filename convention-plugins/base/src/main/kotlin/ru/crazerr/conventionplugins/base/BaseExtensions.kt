package ru.crazerr.conventionplugins.base

import com.android.build.api.dsl.AndroidResources
import com.android.build.api.dsl.BuildFeatures
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.DefaultConfig
import com.android.build.api.dsl.Installation
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.ProductFlavor
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import org.gradle.kotlin.dsl.withType

private typealias AndroidExtensions = CommonExtension<
        out BuildFeatures,
        out BuildType,
        out DefaultConfig,
        out ProductFlavor,
        out AndroidResources,
        out Installation>

private val Project.androidExtension: AndroidExtensions
    get() = extensions.findByType(BaseAppModuleExtension::class)
        ?: extensions.findByType(LibraryExtension::class)
        ?: error(
            "\"Project.androidExtension\" value may be called only from android application" +
                    " or android library gradle script"
        )

val Project.libs: LibrariesForLibs
    get() = the<LibrariesForLibs>()

val Project.projectJavaVersion: JavaVersion
    get() = JavaVersion.toVersion(libs.versions.java.get().toInt())

fun Project.androidConfig(block: AndroidExtensions.() -> Unit): Unit = block(androidExtension)

fun Project.kotlinJvmCompilerOptions(block: KotlinJvmCompilerOptions.() -> Unit) {
    tasks.withType<KotlinJvmCompile>().configureEach {
        compilerOptions(block)
    }
}