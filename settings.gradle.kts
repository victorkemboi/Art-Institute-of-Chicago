pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "Art Space"

include(
    ":android",
    ":desktop",
    ":common",
)

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
