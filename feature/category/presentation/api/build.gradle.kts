plugins {
    id("presentation.plugin")
}

android {
    namespace = "ru.crazerr.feature.category.presentation.api"
}

dependencies {
    api(project(":feature:icon:presentation:api"))
    implementation(project(":feature:category:domain:api"))
}