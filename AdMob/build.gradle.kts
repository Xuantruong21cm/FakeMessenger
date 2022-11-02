plugins {
  id("com.android.library")
  id(Config.Plugins.kotlinAndroid)
  id(Config.Plugins.kotlinKapt)
}

android {

  compileSdk = 33
  defaultConfig {
    minSdk = 28
    targetSdk = 32
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
      )
    }
  }
}

dependencies {

  implementation("androidx.core:core-ktx:1.7.0")
  implementation("androidx.appcompat:appcompat:1.4.2")
  implementation("com.google.android.material:material:1.6.1")
  implementation("com.google.android.gms:play-services-ads:21.2.0")

  // AppLovin Integrate
  implementation("com.applovin:applovin-sdk:+")
  implementation("com.google.android.gms:play-services-ads-identifier:17.1.0")

  androidTestImplementation("androidx.test.ext:junit:1.1.3")
}