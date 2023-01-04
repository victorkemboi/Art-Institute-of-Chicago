group "mes.inc.aic"
version "1.0-SNAPSHOT"

plugins {
    kotlin("multiplatform") version libs.versions.kotlin apply false
    kotlin("android") version libs.versions.kotlin apply false
    id("com.android.application") version libs.versions.android.gradle.plugin apply false
    id("com.android.library") version libs.versions.android.gradle.plugin apply false
    id("org.jetbrains.compose") version libs.versions.compose.multiplatform apply false
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
        config.setFrom("${project.rootDir}/config/detekt.yaml")
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt> detekt@{
        reports {
            xml.required.set(true)
            html.required.set(true)
            txt.required.set(true)
            sarif.required.set(true)
            md.required.set(true)
        }
    }

    dependencies {
        detektPlugins(rootProject.libs.detekt.formatting)
    }

}