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

include(":notifications")
include(":sharing")

include(":commons-android")
include(":commons-compose")
include(":storage-value-kotlin")
include(":storage-value-android")

include(":data")
include(":domain")
include(":strings")
include(":screens:product-list-preview")
include(":screens:product-input-form")
include(":screens:product-list-actions")
include(":screens:final-steps")
include(":screens:clear-notifications-reminder")