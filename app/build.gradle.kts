import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
  id(Config.Plugins.androidApplication)
  id(Config.Plugins.kotlinAndroid)
  id(Config.Plugins.kotlinKapt)
  id(Config.Plugins.navigationSafeArgs)
  id(Config.Plugins.hilt)
  id(Config.Plugins.appLovin)
//  id(Config.Plugins.googleService)
//  id(Config.Plugins.firebaseCrashlytics)
}

applovin {
  apiKey = "x5nodsO_qeATUUt7uw_s7e0oNDXl2tJFE3hnG4ks1J5l8anPmAGiONd7t--f28bXK6SfOUpcJfEmUJE5AhpkXC"
}

android {
  compileSdk = Config.AppConfig.compileSdkVersion

  defaultConfig {
    applicationId = "com.merryblue.fakemessenger"
    minSdk = Config.AppConfig.minSdkVersion
    targetSdk = Config.AppConfig.compileSdkVersion
    versionCode = Config.AppConfig.versionCode
    versionName = Config.AppConfig.versionName

    vectorDrawables.useSupportLibrary = true
    multiDexEnabled = true
    testInstrumentationRunner = Config.AppConfig.testRunner
  }

  buildTypes {
    getByName("debug") {
      manifestPlaceholders["appName"] = "@string/app_name"
      manifestPlaceholders["appIcon"] = "@drawable/icon_app_download"
      manifestPlaceholders["appRoundIcon"] = "@drawable/icon_app_download"

      buildConfigField("String", "API_BASE_URL", Config.Environments.debugBaseUrl)
    }

    getByName("release") {
      isMinifyEnabled = true
      isShrinkResources = true

      manifestPlaceholders["appName"] = "@string/app_name"
      manifestPlaceholders["appIcon"] = "@drawable/icon_app_download"
      manifestPlaceholders["appRoundIcon"] = "@drawable/icon_app_download"

      buildConfigField("String", "API_BASE_URL", Config.Environments.releaseBaseUrl)
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  kotlinOptions {
    jvmTarget = "11"
  }

  dataBinding {
    isEnabled = true
  }

  buildFeatures {
    viewBinding = true
  }
}

dependencies {
  implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

  // Networking
  implementation(Libraries.retrofit)
  implementation(Libraries.retrofitConverter)
  implementation(Libraries.gson)
  implementation(Libraries.interceptor)
  implementation(Libraries.chuckLogging)

  // Utils
  implementation(Libraries.playServices)
  implementation(Libraries.localization)
  implementation(Libraries.multidex)
  implementation(Libraries.lifeData)
  implementation(Libraries.lifecycle)
  implementation(Libraries.viewModelState)
  implementation(Libraries.viewModel)

  // Hilt
  implementation(Libraries.hilt)
  kapt(Libraries.hiltDaggerCompiler)

  // Support
  implementation(Libraries.appCompat)
  implementation(Libraries.coreKtx)
  implementation(Libraries.androidSupport)

  // Kotlin Coroutines
  implementation(Libraries.coroutinesCore)
  implementation(Libraries.coroutinesAndroid)

  // UI
  implementation(Libraries.materialDesign)
  implementation(Libraries.navigationFragment)
  implementation(Libraries.navigationUI)
  implementation(Libraries.alerter)
  implementation(Libraries.coil)
  implementation(Libraries.xrecyclerview)

  // Utils
  implementation(Libraries.permissions)

  // Map
  implementation(Libraries.map)
  implementation(Libraries.playServicesLocation)
  implementation(Libraries.rxLocation)

  // Room
  implementation(Libraries.room)
  kapt(Libraries.roomCompiler)
  implementation(Libraries.roomKtx)

  // Lottie
  implementation(Libraries.lottie)



  //Facebook
  //Debug:    tlECJSgCKpFNV15c7LmHseIzaww=
  //Release:  gjJG2Eu3XTcXm/ewy5UY9ozdXis=
  //          /lMHw5u2HOcBv31gWKmoYhY/wmU=
  implementation("com.facebook.android:facebook-android-sdk:latest.release")

  // request perrmistion
  implementation("com.guolindev.permissionx:permissionx:1.6.4")

  // android networking
  implementation ("com.amitshekhar.android:android-networking:1.0.2")
  implementation(platform("com.google.firebase:firebase-bom:30.1.0"))

  // Firebase crash
//  implementation("com.google.firebase:firebase-crashlytics-ktx:18.3.1")
//  implementation("com.google.firebase:firebase-analytics-ktx")
//  implementation("com.google.firebase:firebase-analytics")

  // In-App Purchase Library
  implementation(Libraries.billing)

  // Project Modules
  implementation(project(Config.Modules.common))
  implementation(project(Config.Modules.data))
  implementation(project(Config.Modules.admob))
}