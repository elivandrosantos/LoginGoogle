plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
}

android {
    namespace = "br.com.ordnavile.logingoogle"
    compileSdk = 34

    defaultConfig {
        applicationId = "br.com.ordnavile.logingoogle"
        minSdk = 23
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
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
//            excludes += "/..."
//            excludes += "META-INF/LICENSE"
//            excludes += "META-INF/*.properties"
//            excludes += "META-INF/AL2.0"
//            excludes += "META-INF/LGPL2.1"
//            excludes += "META-INF/*.kotlin_module"
            excludes += "**/*"

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
    implementation(libs.firebase.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.ui.viewbinding)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("androidx.credentials:credentials:1.2.2")
//    implementation("androidx.credentials:credentials-play-services-auth:1.2.2")

    // Google Play Services Auth
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    // Gmail API
    implementation("com.google.apis:google-api-services-gmail:v1-rev20240520-2.0.0")

    // Google API Client
    implementation("com.google.api-client:google-api-client-android:2.5.1")
    implementation("com.google.http-client:google-http-client-android:1.41.8")
    implementation("com.google.api-client:google-api-client-gson:2.5.1")
    implementation("com.google.http-client:google-http-client-gson:1.41.8")



    implementation("androidx.compose.ui:ui:1.6.7")
    implementation ("com.sun.mail:android-mail:1.6.6")
    implementation ("com.sun.mail:android-activation:1.6.7")
    implementation("io.coil-kt:coil-compose:2.6.0")

    implementation(kotlin("reflect"))

//    implementation ("com.google.android.libraries.identity.googleid:googleid:1.2.2")



}