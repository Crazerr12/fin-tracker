package ru.crazerr.conventionplugins.base

import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope

fun <T : Any> DependencyHandler.implementation(
    dependency: T
) {
    add("implementation", dependency)
}

fun DependencyHandler.debugImplementation(
    dependency: String,
) {
    add("debugImplementation", dependency)
}