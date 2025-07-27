pluginManagement {
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

rootProject.name = "Notes"

include(":assembly")

include(":commons:android")
include(":commons:compose")
include(":commons:resources")

include(":data")
include(":domain")
include(":presentation:notifications")
include(":presentation:sharing")
include(":presentation:screens:product-list-preview")
include(":presentation:screens:product-input-form")
include(":presentation:screens:product-list-actions")
include(":presentation:screens:final-steps")
include(":presentation:screens:clear-notifications-reminder")
include(":presentation:screens:settings")

include(":storage-value-kotlin")
include(":storage-value-android")
