plugins {
    id("presentation.plugin")
}

android {
    namespace = "ru.crazerr.feature.transaction.presentation"
}

dependencies {
    implementation(project(":feature:account:presentation"))
    implementation(project(":feature:category:presentation"))
    implementation(project(":feature:transaction:domain"))
    implementation(project(":feature:transaction:data"))
}