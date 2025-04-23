plugins {
    id("data.plugin")
}

android {
    namespace = "ru.crazerr.feature.icon.data"
}

dependencies {
    implementation(project(":feature:icon:domain"))
    implementation(project(":feature:icon:data:api"))
}