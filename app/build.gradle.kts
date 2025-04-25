import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.hilt.android) // Hilt
    id("kotlin-kapt")
}



android {
    namespace = "com.wamufi.signindemo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.wamufi.signindemo"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val naverClientId: String = gradleLocalProperties(rootDir, providers).getProperty("naver_client_id")
        val naverClientSecret: String = gradleLocalProperties(rootDir, providers).getProperty("naver_client_secret")
        val kakaoAppKey: String = gradleLocalProperties(rootDir, providers).getProperty("kakao_app_key")
        val kakaoManifestAppKey: String = gradleLocalProperties(rootDir, providers).getProperty("kakao_manifest_app_key")

        buildConfigField("String", "NAVER_CLIENT_ID", naverClientId)
        buildConfigField("String", "NAVER_CLIENT_SECRET", naverClientSecret)
        buildConfigField("String", "KAKAO_APP_KEY", kakaoAppKey)
        manifestPlaceholders["KAKAO_MANIFEST_APP_KEY"] = kakaoManifestAppKey
    }

    buildTypes {
        debug {

        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.hilt.android) // Hilt
    implementation(libs.androidx.hilt.navigation.compose) // Hilt
    kapt(libs.hilt.android.compiler) // Hilt
    implementation(libs.coil.compose) // coil

    // Sign in
    implementation(libs.naver.oauth) // 네이버
    implementation(libs.kakao.user) // 카카오
}