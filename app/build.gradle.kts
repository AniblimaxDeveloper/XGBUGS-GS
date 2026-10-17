plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.xgbugs.gs"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.xgbugs.gs"
        minSdk = 23
        targetSdk = 34
        versionCode = 5
        versionName = "5.0"
    }

    // Keep javac and Kotlin bytecode targets aligned. This fixes the
    // compileDebugJavaWithJavac (1.8) vs compileDebugKotlin (17) failure.
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}
