import org.gradle.internal.impldep.bsh.commands.dir
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
//    implementation ("com.github.momo-wallet:mobile-sdk:1.0.7")
//    implementation("com.squareup.okhttp3:okhttp:4.6.0")
//    implementation("commons-codec:commons-codec:1.14")

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
    implementation(fileTree(mapOf("dir" to "D:\\Studying\\Junior\\Semester 2\\Mobile\\ShoppingApp\\ZaloPay", "include" to listOf("*.aar", "*.jar"))))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}