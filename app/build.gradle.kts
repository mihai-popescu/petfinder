@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id(libs.plugins.androidx.navigation.safeargs.kotlin.get().pluginId)
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.petfinder"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.petfinder"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            versionNameSuffix = "-dev"
            isDebuggable = true
        }

        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }

    testOptions {
        // Used for Unit testing Android dependent elements in /test folder
        unitTests.isIncludeAndroidResources  = true
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.retrofit)
    implementation(libs.okhttp3.logging.interceptor)
    implementation(libs.converter.gson)
    implementation(libs.koin)
    implementation(libs.coil)
    implementation(libs.androidx.paging.runtime.ktx)
    testImplementation(libs.junit)
    testImplementation(libs.koin.test.junit4)
    testImplementation(libs.koin.android.test)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.androidx.swiperefreshlayout)
}