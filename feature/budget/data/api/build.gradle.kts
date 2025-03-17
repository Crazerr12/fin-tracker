plugins {
    id("data.plugin")
}

android {
    namespace = "ru.crazerr.feature.budget.data.api"
}

dependencies {
    api(project(":feature:category:data:api"))
    implementation(project(":feature:budget:domain:api"))
}