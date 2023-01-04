group "mes.inc.aic"
version "1.0-SNAPSHOT"

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

plugins {
    kotlin("multiplatform") version libs.versions.kotlin apply false
    kotlin("android") version libs.versions.kotlin apply false
    id("com.android.application") version libs.versions.android.gradle.plugin apply false
    id("com.android.library") version libs.versions.android.gradle.plugin apply false
    id("org.jetbrains.compose") version libs.versions.compose.multiplatform apply false
    alias(libs.plugins.detekt)
}