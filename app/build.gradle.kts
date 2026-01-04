// 1. Plugins ALWAYS go at the very top
plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.musicplayerr"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.musicplayerr"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // SECTION 3: Retrofit (The cause of 'Cannot resolve symbol retrofit2')
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // SECTION 5: RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")
}