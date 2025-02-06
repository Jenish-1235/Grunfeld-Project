pluginManagement ***REMOVED***
    repositories ***REMOVED***
        google ***REMOVED***
            content ***REMOVED***
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
    ***REMOVED***
***REMOVED***
        mavenCentral()
        gradlePluginPortal()
***REMOVED***
***REMOVED***
dependencyResolutionManagement ***REMOVED***
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories ***REMOVED***
        google()
        mavenCentral()
***REMOVED***
***REMOVED***

rootProject.name = "Grunfeld-Project"
include(":app")
