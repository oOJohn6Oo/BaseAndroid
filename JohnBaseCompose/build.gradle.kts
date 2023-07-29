plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}
apply(from = "publish.gradle")

android {
    namespace = "io.john6.johnbase.compose"
    compileSdk = 34

    defaultConfig {
        minSdk = 16
        targetSdk = 34

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
}

dependencies {
    api("androidx.appcompat:appcompat:1.6.1")
    api("androidx.compose.material:material:1.4.3")
    api("androidx.core:core-ktx:1.10.1")
}