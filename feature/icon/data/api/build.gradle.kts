plugins {
    id("data.plugin")
}

android {
    namespace = "ru.crazerr.feature.icon.data.api"
}

dependencies {
    api(project(":feature:icon:domain:api"))
}