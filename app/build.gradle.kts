plugins {
    alias(libs.plugins.android.application)

    id("com.google.gms.google-services")
}



android {
    namespace = "com.example.forfarmer"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.forfarmer"
        minSdk = 23
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
    implementation(libs.firebase.auth)
    implementation(libs.firebase.inappmessaging)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.database)
    implementation(libs.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.firebase.bom)
    implementation(libs.glide)
    implementation(libs.firebase.database)
    implementation("com.airbnb.android:lottie:6.6.1")
    implementation(libs.recyclerview.v131)
    implementation (libs.firebase.auth)
    implementation (libs.play.services.auth)
    implementation(libs.circleimageview)
    implementation(libs.androidx.cardview)
    implementation(libs.material.v190)
    implementation (libs.facebook.login)



}