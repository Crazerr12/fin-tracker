plugins {
    id("domain.plugin")
}

dependencies {
    api(project(":feature:category:domain:api"))
    api(project(":feature:transaction:domain:api"))
}
