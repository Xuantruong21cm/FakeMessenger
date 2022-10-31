plugins {
    id(Config.Plugins.androidLibrary)
    id(Config.Plugins.kotlinAndroid)
    id(Config.Plugins.kotlinKapt)
    id(Config.Plugins.hilt)
}

android {
    compileSdk = Config.AppConfig.compileSdkVersion

    defaultConfig {
        minSdk = Config.AppConfig.minSdkVersion
        targetSdk = Config.AppConfig.compileSdkVersion

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(Libraries.coreKtx)
    implementation(Libraries.retrofit)
    api(Libraries.gson)

    // Hilt
    implementation(Libraries.hilt)
    kapt(Libraries.hiltDaggerCompiler)

    // Room
    implementation(Libraries.room)
    kapt(Libraries.roomCompiler)
    implementation(Libraries.roomKtx)

    // In-App Purchase Library
    implementation(Libraries.billing)

    androidTestImplementation("androidx.test.ext:junit:1.1.3")
}