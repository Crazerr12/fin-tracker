plugins {
    id("data.plugin")
}

android {
    namespace = "ru.crazerr.feature.currency.data.api"
}

dependencies {
    implementation(project(":feature:currency:domain:api"))
}