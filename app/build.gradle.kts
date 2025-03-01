plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
    id("kotlin-kapt")
}

android {
    namespace = "com.tyas.smartfarm"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tyas.smartfarm"
        minSdk = 26
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.auth.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.viewpager2)
    implementation(libs.glide)
    implementation(libs.chip.navigation.bar)
    implementation(libs.blurview)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation (libs.firebase.database)
    implementation (libs.firebase.storage)

    implementation (libs.androidx.datastore.preferences)

    implementation(libs.circleindicator)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor ("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")

    kapt ("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")

    implementation (libs.lottie)
}