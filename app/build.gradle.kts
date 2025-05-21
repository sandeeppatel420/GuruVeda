import org.gradle.kotlin.dsl.implementation

plugins {
    id("com.android.application")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.guruveda"
    compileSdk = 35

    packaging {
        resources {
            excludes += "/META-INF/DEPENDENCIES"
        }
    }

    defaultConfig {
        applicationId = "com.example.guruveda"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
   buildFeatures{
       viewBinding = true
   }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.firestore)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.messaging)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation ("com.github.ismaeldivita:chip-navigation-bar:1.4.0")

    implementation ("com.google.android.gms:play-services-auth:20.7.0")

    implementation ("androidx.media3:media3-exoplayer:1.3.1")
    implementation ("androidx.media3:media3-ui:1.3.1")

    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.2")
    implementation ("com.razorpay:checkout:1.6.33")
    implementation ("com.github.TomLeCollegue:ProgressBar-Library-Android-Kotlin:0.1.1")
    implementation ("com.github.mhiew:android-pdf-viewer:3.2.0-beta.3")

    implementation("com.google.auth:google-auth-library-oauth2-http:1.18.0")


    ///api dependence
    implementation("com.google.code.gson:gson:2.6.2")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ( "com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.retrofit2:adapter-rxjava2:2.9.0")
    implementation( "io.reactivex.rxjava2:rxandroid:2.1.1")
}