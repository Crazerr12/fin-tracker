plugins {
    id("data.plugin")
}

android {
    namespace = "ru.crazerr.feature.budget.data"
}

dependencies {
    implementation(project(":feature:budget:data:api"))
    implementation(project(":feature:budget:domain"))
}
