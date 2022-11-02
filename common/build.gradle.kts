plugins {
  id(Config.Plugins.androidLibrary)
  id(Config.Plugins.kotlinAndroid)
  id(Config.Plugins.kotlinKapt)
  id(Config.Plugins.hilt)
  id(Config.Plugins.navigationSafeArgs)
}

android {
  compileSdk = Config.AppConfig.compileSdkVersion

  defaultConfig {
    minSdk = Config.AppConfig.minSdkVersion

    vectorDrawables.useSupportLibrary = true
  }

  buildTypes {
    getByName("release") {
      proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
      buildConfigField("String", "API_BASE_URL", Config.Environments.releaseBaseUrl)
    }

    getByName("debug") {
      buildConfigField("String", "API_BASE_URL", Config.Environments.debugBaseUrl)
    }
  }

  dataBinding {
    isEnabled = true
  }

  viewBinding {
    isEnabled = true
  }
}

dependencies {

  // Support
  implementation(Libraries.appCompat)
  implementation(Libraries.coreKtx)
  implementation(Libraries.androidSupport)
  implementation(Libraries.lifecycle)
  implementation(Libraries.lifeData)

  // Arch Components
  implementation(Libraries.viewModel)

  // Kotlin Coroutines
  implementation(Libraries.coroutinesCore)
  implementation(Libraries.coroutinesAndroid)

  // UI
  implementation(Libraries.materialDesign)
  implementation(Libraries.navigationFragment)
  implementation(Libraries.navigationUI)
  implementation(Libraries.alerter)
  implementation(Libraries.coil)

  // Utils
  implementation(Libraries.playServices)
  //implementation(Libraries.localization)
  implementation(Libraries.gson)

  // dimens
  implementation(Libraries.sdpAndroid)
  implementation(Libraries.sspAndroid)

  // Hilt
  implementation(Libraries.hilt)
  kapt(Libraries.hiltDaggerCompiler)

  // Map
  implementation(Libraries.map)
  implementation(Libraries.playServicesLocation)
  implementation(Libraries.rxLocation)
  implementation(Libraries.gson)

  // Image
  implementation(Libraries.photo)
  implementation(Libraries.swipeRefresh)
  implementation(Libraries.glide)
  kapt(Libraries.glideCompiler)
  api(Libraries.timber)
  api(Libraries.xrecyclerview)

  // Lottie
  implementation(Libraries.lottie)

  // Room
  implementation(Libraries.room)
  kapt(Libraries.roomCompiler)
  implementation(Libraries.roomKtx)

  // In-App Purchase
  implementation(Libraries.billing)

  //scalarConverter
  implementation(Libraries.scalarConverter)

  // Networking
  implementation(Libraries.retrofit)
  implementation(Libraries.retrofitConverter)
  implementation(Libraries.gson)
  implementation(Libraries.interceptor)
  implementation(Libraries.chuckLogging)

  implementation(project(Config.Modules.data))
  implementation(project(Config.Modules.admob))

  // request perrmistion
  implementation(Libraries.permissions)

  //event bus
  implementation(Libraries.eventBus)

  //localehelper
  implementation(Libraries.localization)

  androidTestImplementation("androidx.test.ext:junit:1.1.3")
}