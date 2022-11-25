object Config {
  object AppConfig {
    const val appId = "org.app.androidbase"
    const val compileSdkVersion = 33
    const val minSdkVersion = 28
    const val versionCode = 104
    const val versionName = "1.0.1"
    const val testRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  object Dependencies {
    const val jitPackURL = "https://jitpack.io"
    const val gradleVersion = "com.android.tools.build:gradle:${Versions.gradleVersion}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val navigationSafeArgs =
      "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.androidNavigation}"
    const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hiltVersion}"
    const val googleService = "com.google.gms:google-services:${Versions.googleService}"
    const val crashlytics = "com.google.firebase:firebase-crashlytics-gradle:${Versions.crashlytics}"
  }

  object Plugins {
    const val androidApplication = "com.android.application"
    const val kotlinAndroid = "kotlin-android"
    const val kotlinKapt = "kotlin-kapt"
    const val navigationSafeArgs = "androidx.navigation.safeargs.kotlin"
    const val hilt = "dagger.hilt.android.plugin"
    const val androidLibrary = "com.android.library"
    const val kotlinJvm = "org.jetbrains.kotlin.jvm"
    const val ktLint = "org.jlleitschuh.gradle.ktlint"
    const val appLovin = "applovin-quality-service"
    const val googleService = "com.google.gms.google-services"
    const val firebaseCrashlytics = "com.google.firebase.crashlytics"
  }

  object Modules {
    const val common = ":common"
    const val data = ":data"
    const val admob = ":AdMob"
  }

  object Environments {
    const val debugBaseUrl = "\"https://manager.merryblue.llc/\""
    const val releaseBaseUrl = "\"https://manager.merryblue.llc/\""
  }
}