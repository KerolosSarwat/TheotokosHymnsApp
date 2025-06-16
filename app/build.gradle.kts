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
        versionCode = 11
        versionName = "1.1"

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false // Disable for faster debug builds
            isDebuggable = true
            isShrinkResources = false
        }
        release {
            isMinifyEnabled = true // Enable for production builds
            isShrinkResources = false
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
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)
    // If using Crashlytics, uncomment the line below
    //implementation(libs.firebase.crashlytics)

    implementation("com.google.zxing:core:3.4.1")
    implementation(libs.zxing.android.embedded)
    implementation(libs.mhiew.android.pdf.viewer)
    implementation(libs.work.runtime.v271)
    implementation(libs.guava)
    implementation(libs.gson)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.gridlayout)
    implementation(libs.preference)
    implementation(libs.play.services.base)
//    implementation(libs.firebase.crashlytics.buildtools)
//    implementation(libs.material.v1120)
    implementation(libs.viewpager2)
    implementation(libs.github.glide) // For image loading
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    implementation(libs.picasso)
    implementation(libs.androidsvg)
    implementation (libs.exoplayer)
    implementation (libs.exoplayer.core)
    implementation (libs.exoplayer.dash)
    implementation (libs.exoplayer.ui)
    implementation(libs.cardview)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Remove or comment out test dependencies unless you are actively using them
    // testImplementation(libs.junit)
    // androidTestImplementation(libs.ext.junit)
    // androidTestImplementation(libs.espresso.core)
}