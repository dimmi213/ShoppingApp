plugins {
    alias(libs.plugins.androidApplication)
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
}

dependencies {

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
    implementation(libs.notification)
    implementation(libs.eventbus)
    implementation(libs.gson)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}