plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.googleGmsGoogleServices)
}

android {
    namespace = "com.example.payment"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.payment"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    implementation(libs.vectordrawable)
    implementation(libs.cardview)
    implementation(libs.legacy.support.v4)
    implementation(libs.firebase.auth)

    implementation ("com.google.mlkit:barcode-scanning:17.0.0")
    implementation ("androidx.camera:camera-core:1.2.0")
    implementation ("androidx.camera:camera-camera2:1.2.0")
    implementation ("androidx.camera:camera-lifecycle:1.2.0")
    implementation ("androidx.camera:camera-view:1.0.0-alpha31")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation ("androidx.camera:camera-core:1.1.0-beta03")
    implementation ("androidx.camera:camera-camera2:1.1.0-beta03")
    implementation ("androidx.camera:camera-lifecycle:1.1.0-beta03")
    implementation ("androidx.camera:camera-view:1.0.0-alpha30")




}