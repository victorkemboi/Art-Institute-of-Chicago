plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("com.google.devtools.ksp")
    id("com.squareup.sqldelight")
}

group = "mes.inc.aic"
version = "1.0.0"

kotlin {
    android()
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.foundation)
                api(compose.material)
                api(compose.runtime)
                api(libs.koin.core)
//                api(libs.koin.annotations)
//                ksp(libs.koin.ksp.compiler)
                api(libs.koin.test)
            }
            kotlin.srcDirs("build/generated/ksp/main/kotlin")
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                api(libs.app.compat)
                api(libs.androidx.core)
                implementation(libs.sqldelight.android.driver)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(libs.junit.vintage.engine)
            }
        }
        val desktopMain by getting {
            dependencies {
                api(compose.preview)
                implementation(libs.sqldelight.sqlite.driver)
            }
        }
        val desktopTest by getting
    }
}

android {
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    namespace = "mes.inc.aic.common"
    defaultConfig {
        minSdk = 24
        targetSdk = 33
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    add("kspCommonMainMetadata", project(":common"))
}

sqldelight {
    database("ArtSpaceDatabase") {
        packageName = "mes.inc.aic.database"
        sourceFolders = listOf("sqldelight")
    }
}