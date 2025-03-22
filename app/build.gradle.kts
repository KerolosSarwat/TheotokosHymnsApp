plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    // If using Crashlytics, uncomment the line below
     //alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.example.theotokos" // Or com.mariam.theotokos - make sure it's correct
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mariam.theotokos" // Make sure this is correct
        minSdk = 24 // Consider raising if you don't need very old device support
        targetSdk = 34
        versionCode = 7
        versionName = "1.7"

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false // Disable for faster debug builds
            isDebuggable = true
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // Enable View Binding or Data Binding if you are using them
    buildFeatures {
        viewBinding = true // Or dataBinding true
    }
}

dependencies {
    // Firebase BOM (Bill of Materials) - MUST BE FIRST
    implementation(platform(libs.firebase.bom))

    // Then, include only the Firebase components you need:
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)
    // If using Crashlytics, uncomment the line below
    //implementation(libs.firebase.crashlytics)

    implementation("com.google.zxing:core:3.4.1")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.github.mhiew:android-pdf-viewer:3.2.0-beta.3")
    implementation("androidx.work:work-runtime:2.7.1")
    implementation(libs.guava)
    implementation(libs.gson)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.gridlayout)
    implementation(libs.preference)
    implementation(libs.play.services.base)
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.material.v1120)
    implementation(libs.viewpager2)
    implementation(libs.github.glide) // For image loading
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
//    implementation (libs.play.services)
    implementation ("com.google.android.exoplayer:exoplayer:2.19.1")
    implementation ("com.google.android.exoplayer:exoplayer-core:2.19.1")
    implementation ("com.google.android.exoplayer:exoplayer-dash:2.19.1")
    implementation ("com.google.android.exoplayer:exoplayer-ui:2.19.1")
    implementation(libs.cardview)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Remove or comment out test dependencies unless you are actively using them
    // testImplementation(libs.junit)
    // androidTestImplementation(libs.ext.junit)
    // androidTestImplementation(libs.espresso.core)
}