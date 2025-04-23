plugins {
    id("data.plugin")
}

android {
    namespace = "ru.crazerr.feature.account.data.api"
}

dependencies {
    api(project(":feature:currency:data:api"))
    api(project(":feature:icon:data:api"))
    implementation(project(":feature:account:domain:api"))
}