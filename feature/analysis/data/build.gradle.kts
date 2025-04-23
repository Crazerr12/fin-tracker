plugins {
    id("data.plugin")
}

android {
    namespace = "ru.crazerr.feature.analysis.data"
}

dependencies {
    implementation(project(":feature:analysis:domain"))
    implementation(project(":feature:category:data:api"))
}