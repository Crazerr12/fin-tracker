plugins {
    id("presentation.plugin")
}

android {
    namespace = "ru.crazerr.feature.account.presentation"
}

dependencies {
    implementation(project(":feature:account:data"))
    implementation(project(":feature:account:domain"))
}