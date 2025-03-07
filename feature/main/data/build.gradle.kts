plugins {
    id("data.plugin")
}

android {
    namespace = "ru.crazerr.feature.main.data"
}

dependencies {
    implementation(project(":feature:account:data:api"))
    implementation(project(":feature:main:domain"))
}