plugins {
    id("presentation.plugin")
}

android {
    namespace = "ru.crazerr.feature.category.presentation"
}

dependencies {
    implementation(project(":feature:category:domain"))
    implementation(project(":feature:category:data"))
}