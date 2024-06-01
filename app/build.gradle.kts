plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)

    id("com.google.gms.google-services") //firebase 앱 등록

    id("kotlin-kapt") // Kotlin Annotation Processing Tool
}

android {
    namespace = "com.example.greenhouse"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.greenhouse"
        minSdk = 24
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

    // [뷰 바인딩]
    viewBinding{
        enable = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat) // appcompat에 이미 뷰 페이저 포함되어 있지만
    implementation("androidx.viewpager2:viewpager2:1.0.0") // 조금 더 잘 동작하도록 뷰 페이저 명시적으로 implementation
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // [firebase 연동]
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))

    // [firebase 인증 라이브러리]
    implementation("com.google.firebase:firebase-auth-ktx:23.0.0")

    // [MultiDexApplication: Dex 여러개 허용]
    implementation("androidx.multidex:multidex:2.0.1")

    // [구글 인증 로그인 라이브러리]
    implementation("com.google.android.gms:play-services-auth:21.1.1")

    // [Retrofit2]
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // [tikxml]
    implementation("com.tickaroo.tikxml:annotation:0.8.13")
    implementation("com.tickaroo.tikxml:core:0.8.13")
    implementation("com.tickaroo.tikxml:retrofit-converter:0.8.13")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    kapt ("com.tickaroo.tikxml:processor:0.8.13")

    // [이미지 처리-Glide 라이브러리]
    implementation("com.github.bumptech.glide:glide:4.12.0")
}