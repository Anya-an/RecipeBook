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

rootProject.name = "RecipeBook"
include(":app")
include(":core")
include(":feature_find")
include(":feature_add_recipe")
include(":db")
include(":db_impl")
include(":feature_book")
include(":feature_recipe")
include(":ui_kit")
include(":network")
