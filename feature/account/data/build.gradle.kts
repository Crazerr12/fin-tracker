plugins {
    id("data.plugin")
}

android {
    namespace = "ru.crazerr.feature.account.data"
}

dependencies {
    implementation(project(":feature:account:domain"))
}