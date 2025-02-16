import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import ru.crazerr.conventionplugins.base.kotlinJvmCompilerOptions
import ru.crazerr.conventionplugins.base.libs
import ru.crazerr.conventionplugins.base.projectJavaVersion

plugins {
    id("java-library")
}

java {
    sourceCompatibility = projectJavaVersion
    targetCompatibility = projectJavaVersion
}

kotlinJvmCompilerOptions {
    jvmTarget.set(JvmTarget.fromTarget(projectJavaVersion.toString()))
}