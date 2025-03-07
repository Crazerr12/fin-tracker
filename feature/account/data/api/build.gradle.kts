plugins {
    id("data.plugin")
}

android {
    namespace = "ru.crazerr.feature.account.data.api"
}

dependencies {
    api(project(":feature:currency:data:api"))
    implementation(project(":feature:account:domain:api"))
}