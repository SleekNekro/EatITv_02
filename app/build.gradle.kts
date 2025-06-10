plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.estoyDeprimido"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.estoyDeprimido"
        minSdk = 27
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
    buildFeatures {
        compose = true
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
    implementation(libs.androidx.material)
    implementation("com.google.android.material:material:1.12.0")

    //voyager
    implementation(libs.voyager.navigator)
    implementation(libs.voyager.tab.navigator)
    implementation(libs.voyager.transitions)

    // Corrutinas
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    // ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.0")


    //ktor y serializacion
    implementation(libs.ktor.client.core.jvm)
    implementation(libs.ktor.client.content.negotiation.jvm)
    implementation(libs.kotlinx.serialization.json.jvm)
    implementation("io.ktor:ktor-client-cio-jvm:3.1.3")

    implementation ("androidx.compose.runtime:runtime-livedata")

    implementation("io.ktor:ktor-client-auth:2.3.0")

    implementation("io.ktor:ktor-client-logging:2.3.0")

    implementation("io.ktor:ktor-client-cio:2.3.0")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation(libs.androidx.paging.common.android)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}