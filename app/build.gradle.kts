import java.util.Properties

plugins ***REMOVED***
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android) version "2.1.0"
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(platform("io.github.jan-tennert.supabase:bom:3.1.1"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.ktor:ktor-client-android:3.0.3")
***REMOVED***