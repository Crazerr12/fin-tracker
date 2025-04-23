plugins {
    id("data.plugin")
}

android {
    namespace = "ru.crazerr.feature.category.data.api"
}

dependencies {
    implementation(project(":feature:category:domain:api"))
    api(project(":feature:icon:data:api"))
}