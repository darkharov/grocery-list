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
include(":commons-android")
include(":commons-compose")
include(":data")
include(":domain")
include(":notifications")
include(":product-input-form")
include(":strings")
include(":product-list-preview")
include(":product-list-actions")
include(":final-steps")
include(":clear-notifications-reminder")
include(":storage-value-kotlin")
include(":storage-value-android")
include(":sharing")
