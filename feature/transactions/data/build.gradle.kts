plugins {
    id("data.plugin")
}

android {
    namespace = "ru.crazerr.feature.transactions.data"
}

dependencies {
    implementation(project(":feature:transaction:data:api"))
    implementation(project(":feature:account:data:api"))
    implementation(project(":feature:category:data:api"))
    implementation(project(":feature:transactions:domain"))
}
