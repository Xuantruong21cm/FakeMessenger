// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
  repositories {
    google()
    mavenCentral()
    maven { url = uri("https://artifacts.applovin.com/android") }
  }
  dependencies {
    classpath(Config.Dependencies.gradleVersion)
    classpath(Config.Dependencies.kotlin)
    classpath(Config.Dependencies.navigationSafeArgs)
    classpath(Config.Dependencies.hilt)
    classpath( "com.applovin.quality:AppLovinQualityServiceGradlePlugin:+")
    classpath(Config.Dependencies.googleService)
    classpath(Config.Dependencies.crashlytics)
  }
}

plugins {
  id(Config.Plugins.ktLint) version Versions.ktLint
}

subprojects {
  apply(plugin = Config.Plugins.ktLint) // To apply ktLint to all included modules

  repositories {
    mavenCentral()
  }

  configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    debug.set(true)
  }
}

allprojects {
  repositories {
    google()
    mavenCentral()
    jcenter()
    maven(url = Config.Dependencies.jitPackURL)
  }
}

tasks.register("clean", Delete::class) {
  delete(rootProject.buildDir)
}