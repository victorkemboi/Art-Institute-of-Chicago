group = "mes.inc.aic"
version = "1.0.0"

plugins {
    kotlin("multiplatform") version libs.versions.kotlin apply false
    kotlin("android") version libs.versions.kotlin apply false
    id("com.android.application") version libs.versions.android.gradle.plugin apply false
    id("com.android.library") version libs.versions.android.gradle.plugin apply false
    id("org.jetbrains.compose") version libs.versions.compose.multiplatform apply false
    id("com.google.devtools.ksp") version libs.versions.ksp apply false
    alias(libs.plugins.detekt)
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    apply(plugin = "io.gitlab.arturbosch.detekt")

    detekt {
        parallel = true
        ignoreFailures = false
        autoCorrect = true
        buildUponDefaultConfig = true
        config.setFrom("${project.rootDir}/configs/detekt.yaml")
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt> detekt@{
//        exclude("**/build/**")
        exclude("resources/")
        exclude("build/")
        reports {
            xml.required.set(true)
            html.required.set(true)
            txt.required.set(false)
            sarif.required.set(false)
            md.required.set(false)
        }
    }

    dependencies {
        detektPlugins(rootProject.libs.detekt.formatting)
    }
}

buildscript {
    dependencies {
        classpath(libs.gradle.sqldelight)
    }
}