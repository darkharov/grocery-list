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
include(":commons:storage-value:kotlin")
include(":commons:storage-value:android")
include(":data")
include(":domain")
include(":presentation:notifications")
include(":presentation:ui:main-activity")
include(":presentation:ui:screens:product-list-preview")
include(":presentation:ui:screens:product-input-form")
include(":presentation:ui:screens:product-list-actions")
include(":presentation:ui:screens:final-steps")
include(":presentation:ui:screens:clear-notifications-reminder")
include(":presentation:ui:screens:settings")
include(":commons:kotlin")
