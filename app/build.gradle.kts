import org.jetbrains.kotlin.storage.CacheResetOnProcessCanceled.enabled

plugins {
//    alias(libs.plugins.androidApplication)
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.shopping"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.shopping"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.android.gms:play-services-auth:21.1.1")
    implementation("com.google.firebase:firebase-firestore:25.0.0")
    implementation ("com.facebook.android:facebook-android-sdk:[4,5)")
    implementation ("com.squareup.picasso:picasso:2.8")
    implementation ("com.android.volley:volley:1.2.1")
    implementation ("com.google.android.material:material:1.9.0")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.readableBottomBar)
    implementation(libs.retrofit)
    implementation(libs.converterGson)
    implementation(libs.multidex)
    implementation(libs.glide)
    implementation(libs.rxjava)
    implementation(libs.rxandroid)
    implementation(libs.rxjava3RetrofitAdapter)
    implementation(libs.firebase.database)
    implementation(libs.notification)
    implementation(libs.eventbus)
    implementation(libs.gson)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}