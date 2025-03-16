plugins {
    id("data.plugin")
}

android {
    namespace = "ru.crazerr.feature.transaction.data.api"
}

dependencies {
    api(project(":feature:category:data:api"))
    api(project(":feature:account:data:api"))
    implementation(project(":feature:transaction:domain:api"))
}