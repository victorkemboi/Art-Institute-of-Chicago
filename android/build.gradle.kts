plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
}

group = "mes.inc.aic"
version = "1.0.0"

android {
    compileSdk = 33
    namespace = "mes.inc.aic.android"
    defaultConfig {
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = project.version.toString()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":common"))
    implementation(libs.activity.compose)
}