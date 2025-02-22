plugins {
    id("data.plugin")
}

android {
    namespace = "ru.crazerr.feature.category.data"
}

dependencies {
    implementation(project(":feature:category:domain"))
}