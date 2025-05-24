plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.decompiledapk"
    compileSdk = 34

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.decompiledapk"
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
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += setOf(
                "META-INF/*.kotlin_module",
                "META-INF/NOTICE.md",
                "META-INF/LICENSE.md",
                "META-INF/LICENSE",
                "META-INF/NOTICE",
                "META-INF/*.kotlin_module"
            )
        }
    }
}

dependencies {

    implementation("com.github.felHR85:UsbSerial:6.0.6")

    implementation("com.github.mik3y:usb-serial-for-android:3.8.1")
    implementation("org.osmdroid:osmdroid-android:6.1.16")
    implementation("org.apache.commons:commons-lang3:3.8.1")
    implementation("com.github.MKergall:osmbonuspack:6.9.0")

    implementation("com.github.jkwiecien:EasyImage:3.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Latest stable version as of now
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Matches Retrofit version
    implementation("com.squareup.okhttp3:okhttp:4.9.2") // Compatible with Retrofit 2.9.0
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.2") // Matches OkHttp version
    implementation("net.bytebuddy:byte-buddy:1.12.20")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("org.osmdroid:osmdroid-android:6.1.16")

    implementation("com.github.bumptech.glide:glide:4.9.0")
    implementation("com.github.bumptech.glide:annotations:4.9.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.9.0")

    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.google.android.material:material:1.1.0")
    implementation("androidx.databinding:databinding-compiler:7.2.1")
    implementation("androidx.databinding:databinding-compiler:7.2.1")
    implementation("com.google.firebase:firebase-core:16.0.8")

    implementation("androidx.multidex:multidex:2.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    implementation("com.google.dagger:dagger:2.51")
    kapt("com.google.dagger:dagger-compiler:2.51")

    implementation("com.google.dagger:dagger-android:2.51")
    implementation("com.google.dagger:dagger-android-support:2.51")
    kapt("com.google.dagger:dagger-android-processor:2.51")

    implementation("androidx.room:room-runtime:2.6.1")
    implementation("id.zelory:compressor:3.0.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")

    ksp("androidx.room:room-compiler:2.6.1")

    implementation("androidx.room:room-ktx:2.6.1")

    implementation("androidx.legacy:legacy-preference-v14:1.0.0")
    implementation("androidx.preference:preference:1.0.0")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

   // implementation("com.github.chuckerteam.chucker:library:3.5.0")
}