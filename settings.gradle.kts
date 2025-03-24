pluginManagement {
    includeBuild("convention-plugins/base")
    includeBuild("convention-plugins/project")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "FinTracker"
include(":app")

include(":core:network")
include(":core:root")
include(":core:database")
include(":core:utils")

include(":feature:auth:data")
include(":feature:auth:domain")
include(":feature:auth:presentation")

include(":feature:main:data")
include(":feature:main:domain")
include(":feature:main:presentation")

include(":feature:transactions:data")
include(":feature:transactions:domain")
include(":feature:transactions:presentation")

include(":feature:analysis:data")
include(":feature:analysis:domain")
include(":feature:analysis:presentation")

include(":feature:budget:data")
include(":feature:budget:data:api")
include(":feature:budget:domain")
include(":feature:budget:domain:api")
include(":feature:budget:presentation")

include(":feature:profile:data")
include(":feature:profile:domain")
include(":feature:profile:presentation")

include(":feature:transaction:data")
include(":feature:transaction:data:api")
include(":feature:transaction:domain")
include(":feature:transaction:domain:api")
include(":feature:transaction:presentation")

include(":feature:currency:data:api")
include(":feature:currency:domain:api")

include(":feature:account:data")
include(":feature:account:data:api")
include(":feature:account:domain")
include(":feature:account:domain:api")

include(":feature:account:presentation")
include(":feature:category:data")
include(":feature:category:data:api")
include(":feature:category:domain")
include(":feature:category:domain:api")

include(":feature:category:presentation")

include(":feature:budgets:data")
include(":feature:budgets:domain")
include(":feature:budgets:presentation")
