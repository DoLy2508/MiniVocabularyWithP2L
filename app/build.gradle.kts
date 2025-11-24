plugins {
    alias(libs.plugins.android.application)
    // được thêm

    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)  // ✅ Dùng alias từ libs.versions.toml
}

android {
    namespace = "com.example.minivocabularywithp2l"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.minivocabularywithp2l"
        minSdk = 24
        targetSdk = 36
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
    // thêm đoạn này
    kotlinOptions {
        jvmTarget = "11"  // 👈 Thêm dòng này
    }
    // 👇 Cấu hình toolchain để Gradle tự dùng JDK 11 khi build
    kotlin {
        jvmToolchain(11)
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // được thêm mới Room
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    testImplementation("androidx.room:room-testing:2.6.1")
}


