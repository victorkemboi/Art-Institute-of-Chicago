group = "mes.inc.aic"
version = "1.0.0"

plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    with(libs) {
        kotlin("multiplatform") version versions.kotlin apply false
        kotlin("android") version versions.kotlin apply false
        id("com.android.application") version versions.android.gradle.plugin apply false
        id("com.android.library") version versions.android.gradle.plugin apply false
        id("org.jetbrains.compose") version versions.compose.multiplatform apply false
        id("com.google.devtools.ksp") version versions.ksp apply false
        alias(plugins.detekt)
    }
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
        setSource(files(project.projectDir))
        exclude("**/build/**")
        exclude {
            it.file.relativeTo(projectDir).startsWith(project.buildDir.relativeTo(projectDir))
        }
        reports {
            xml.required.set(true)
            html.required.set(true)
            txt.required.set(false)
            sarif.required.set(false)
            md.required.set(false)
        }
    }

    tasks.register("detektAll") {
        dependsOn(tasks.withType<io.gitlab.arturbosch.detekt.Detekt>())
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