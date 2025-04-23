plugins {
    id("domain.plugin")
}

dependencies {
    api(project(":feature:currency:domain:api"))
    api(project(":feature:icon:domain:api"))
}