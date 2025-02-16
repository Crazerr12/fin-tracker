plugins {
    id("presentation.plugin")
}

android {
    namespace = "ru.crazerr.feature.transaction.presentation"
}

dependencies {
    implementation(project(":feature:transaction:domain"))
}