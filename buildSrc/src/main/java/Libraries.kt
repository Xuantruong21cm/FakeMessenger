object Libraries {

  // Support
  const val appCompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
  const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
  const val androidSupport = "androidx.legacy:legacy-support-v4:${Versions.supportVersion}"

  // Kotlin
  const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"

  // Java
  const val javaInject = "javax.inject:javax.inject:${Versions.javaInject}"

  // Arch Components
  const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
  const val lifeData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
  const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
  const val viewModelState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.lifecycle}"

  // Kotlin Coroutines
  const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
  const val coroutinesAndroid =
    "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}"

  // Networking
  const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
  const val retrofitConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
  const val gson = "com.google.code.gson:gson:${Versions.gson}"
  const val interceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.interceptor}"
  const val chuckLogging = "com.readystatesoftware.chuck:library:${Versions.chuckLogging}"

  // UI
  const val materialDesign = "com.google.android.material:material:${Versions.materialDesign}"
  const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.androidNavigation}"
  const val navigationUI = "androidx.navigation:navigation-ui-ktx:${Versions.androidNavigation}"
  const val alerter = "com.github.tapadoo:alerter:${Versions.alerter}"
  const val coil = "io.coil-kt:coil:${Versions.coil}"
  const val photo = "com.github.chrisbanes:PhotoView:${Versions.photo}"
  const val swipeRefresh = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefresh}"
  const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
  const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
  const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
  const val xrecyclerview = "androidx.recyclerview:recyclerview:${Versions.xrecyclerview}"

  // Utils
  const val playServices = "com.google.android.gms:play-services-auth:${Versions.playServices}"
  const val localization = "com.zeugmasolutions.localehelper:locale-helper-android:${Versions.localization}"
  const val multidex = "androidx.multidex:multidex:${Versions.multidex}"

  // Hilt
  const val hilt = "com.google.dagger:hilt-android:${Versions.hiltVersion}"
  const val hiltDaggerCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hiltVersion}"

  // Map
  const val map = "com.google.android.gms:play-services-maps:${Versions.map}"
  const val playServicesLocation =
    "com.google.android.gms:play-services-location:${Versions.playServicesLocation}"
  const val rxLocation = "com.github.ShabanKamell:RxLocation:${Versions.rxLocation}"

  // Room
  const val room = "androidx.room:room-runtime:${Versions.room}"
  const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
  const val roomKtx = "androidx.room:room-ktx:${Versions.room}"

  // Lottie
  const val lottie = "com.airbnb.android:lottie:${Versions.lottie}"

  // Billing
  const val billing = "com.android.billingclient:billing-ktx:${Versions.billing}"

  //scalarConverter
  const val scalarConverter =
    "com.squareup.retrofit2:converter-scalars:${Versions.scalarConverter}"

  const val sdpAndroid = "com.intuit.sdp:sdp-android:${Versions.sdpAndroid}"
  const val sspAndroid = "com.intuit.ssp:ssp-android:${Versions.sspAndroid}"
 
  //eventbus
  const val eventBus = "org.greenrobot:eventbus:${Versions.eventBus}"

  //Permission
  const val permissions = "com.guolindev.permissionx:permissionx:${Versions.permissions}"

  //Exoplayer
  const val exoplayer = "com.google.android.exoplayer:exoplayer:${Versions.exoplayer}"

  //CameraX
  const val cameraXCore = "androidx.camera:camera-core:${Versions.cameraX}"
  const val camera2 = "androidx.camera:camera-camera2:${Versions.cameraX}"
  const val cameraXLifecycle = "androidx.camera:camera-lifecycle:${Versions.cameraX}"
  const val cameraXView = "androidx.camera:camera-view:${Versions.cameraX}"


}