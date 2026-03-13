plugins {
    id("data.plugin")
}

android {
    namespace = "ru.crazerr.feature.settings.data"
}

dependencies {
    implementation(project(":feature:category:data:api"))
    implementation(project(":feature:settings:domain"))
}