plugins {
    id("data.plugin")
}

android {
    namespace = "ru.crazerr.feature.budgets.data"
}

dependencies {
    implementation(project(":feature:budget:data:api"))
    implementation(project(":feature:budgets:domain"))
}