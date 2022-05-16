// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val kotlin_version by extra { "1.6.21" }
    val hilt_version by extra { "2.42" }
    val retrofit_version by extra { "2.9.0" }
    val markdown_version by extra { "0.19.0" }
    val navigation_version by extra { "2.4.2" }
    val lifecycle_version by extra { "2.4.1" }

    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:$hilt_version")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlin_version")
    }
}

plugins {
    id("com.android.application") version "7.2.0" apply false
    id("com.android.library") version "7.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.6.21" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.21" apply false
}

tasks.register(name = "clean", type = Delete::class) {
    delete(rootProject.buildDir)
}