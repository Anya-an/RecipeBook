plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.recipeBook"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.recipeBook"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(project(":core"))
    implementation(project(":feature_find"))
    implementation(project(":feature_add_recipe"))
    implementation(project(":db"))
    implementation(project(":feature_book"))
    implementation(project(":feature_recipe"))
    // implementation(project(":db"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Hilt dependencies
    /*implementation("com.google.dagger:hilt-android:2.48") // Hilt runtime
    kapt("com.google.dagger:hilt-android-compiler:2.48") // Hilt compiler

    // Hilt ViewModel integration
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")
    kapt("androidx.hilt:hilt-compiler:1.0.0")*/
    // Hilt dependencies
    implementation(libs.hiltAndroid)
    kapt(libs.hiltCompiler)

    // Hilt ViewModel
    //implementation(libs.hiltLifecycleViewmodel)
   // kapt(libs.hiltCompilerLifecycle)

    // Room dependencies (если нужно использовать напрямую)
    implementation(libs.roomRuntime)
    implementation(libs.roomKtx)
    kapt(libs.roomCompiler)

    // Core Hilt
    //implementation("com.google.dagger:hilt-android:2.44")
    //kapt("com.google.dagger:hilt-compiler:2.44")

    // Если используете ViewModel
        //implementation("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")
    //kapt("androidx.hilt:hilt-compiler:1.0.0")

    // Для работы с Compose
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // Room и Hilt
    //implementation("androidx.room:room-runtime:2.5.0")
   // kapt("androidx.room:room-compiler:2.5.0")
    //implementation("androidx.room:room-ktx:2.5.0")

    // Lifecycle (ViewModel, LiveData)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
}