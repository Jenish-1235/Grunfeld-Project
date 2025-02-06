plugins ***REMOVED***
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
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
***REMOVED***