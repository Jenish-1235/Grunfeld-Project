import java.util.Properties

plugins ***REMOVED***
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android) version "2.1.0"
    kotlin("plugin.serialization") version "1.8.20"
    id("kotlin-kapt")
    id("com.google.gms.google-services")
***REMOVED***

android ***REMOVED***
    namespace = "com.android.grunfeld_project"
    compileSdk = 35

    defaultConfig ***REMOVED***
        applicationId = "com.android.grunfeld_project"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties().apply ***REMOVED***
            load(rootProject.file("local.properties").inputStream())
***REMOVED***
        buildConfigField("String", "BASE_URL", "\"$***REMOVED***properties.getProperty("supabase.url")***REMOVED***\"")
        buildConfigField("String", "ANON_KEY", "\"$***REMOVED***properties.getProperty("supabase.anon.key")***REMOVED***\"")
        buildConfigField("String", "PEXEL_API_KEY", "\"$***REMOVED***properties.getProperty("pexels.api.key")***REMOVED***\"")
***REMOVED***

    buildTypes ***REMOVED***
        release ***REMOVED***
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
***REMOVED***
***REMOVED***
    compileOptions ***REMOVED***
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
***REMOVED***
    kotlinOptions ***REMOVED***
        jvmTarget = "11"
***REMOVED***
    buildFeatures ***REMOVED***
        buildConfig = true
***REMOVED***
***REMOVED***

dependencies ***REMOVED***
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.messaging.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.squareup.picasso:picasso:2.8")
    implementation(platform("io.github.jan-tennert.supabase:bom:3.1.1"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.ktor:ktor-client-android:3.0.3")
    implementation("com.github.bumptech.glide:glide:4.15.1") // Or latest
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Or latest
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Or latest
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0") // For debugging (optional but recommended)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4") // Or latest for coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation ("com.google.code.gson:gson:2.9.1")
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("io.coil-kt:coil:2.3.0")       // or latest
    implementation("io.coil-kt:coil-svg:2.3.0")


***REMOVED***
