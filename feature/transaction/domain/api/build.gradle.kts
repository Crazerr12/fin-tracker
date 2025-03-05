plugins {
    id("domain.plugin")
}

dependencies {
    api(project(":feature:account:domain:api"))
    api(project(":feature:category:domain:api"))
}