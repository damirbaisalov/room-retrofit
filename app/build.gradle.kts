import org.gradle.internal.impldep.com.google.api.client.googleapis.testing.auth.oauth2.MockGoogleCredential.ACCESS_TOKEN
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}
val localProperties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

val accessToken: String? = localProperties.getProperty("accessToken")
val apiKey: String? = localProperties.getProperty("apiKey")
val posterPath: String? = localProperties.getProperty("posterPath")

android {
    namespace = "kz.app.roomretrofit"
    compileSdk = 34

    defaultConfig {
        applicationId = "kz.app.roomretrofit"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "accessToken", "\"${accessToken}\"")
            buildConfigField("String", "apiKey", "\"${apiKey}\"")
            buildConfigField("String", "posterPath", "\"${posterPath}\"")
        }
        debug {
            buildConfigField("String", "accessToken", "\"${accessToken}\"")
            buildConfigField("String", "apiKey", "\"${apiKey}\"")
            buildConfigField("String", "posterPath", "\"${posterPath}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.swipeRefreshLayout)
    implementation(libs.glide)
    ksp(libs.room.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}