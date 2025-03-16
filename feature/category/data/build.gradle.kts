plugins {
    id("data.plugin")
}

android {
    namespace = "ru.crazerr.feature.category.data"
}

dependencies {
    implementation(project(":feature:category:data:api"))
    implementation(project(":feature:category:domain"))
}