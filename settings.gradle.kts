pluginManagement {
    includeBuild("build-logic")
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
        gradlePluginPortal()
        maven { setUrl("https://maven-other.tuya.com/repository/maven-releases/") }
        maven { setUrl("https://maven-other.tuya.com/repository/maven-commercial-releases/") }
        maven { setUrl("https://jitpack.io") }
        maven { setUrl("https://maven.aliyun.com/repository/jcenter") }

    }
}

rootProject.name = "LdvAITableLight"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":aars:nfc")
include(":core:log")
include(":core:ui")
include(":core:utils")
//include(":core:database")
include(":core:log-no-op")
include(":core:tuya")
include(":aars:tuya")
