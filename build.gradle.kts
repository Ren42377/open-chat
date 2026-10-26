buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        // AGP 9 ships built-in Kotlin support with an older default KGP.
        // Align it with the catalog Kotlin version.
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.4.21")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
