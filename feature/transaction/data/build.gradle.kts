import ru.crazerr.conventionplugins.base.implementation

plugins {
    id("data.plugin")
}

android {
    namespace = "ru.crazerr.feature.transaction.data"
}

dependencies {
    implementation(project(":feature:account:data:api"))
    implementation(project(":feature:transaction:domain"))
}