plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "${Configs.APPLICATION_ID}.strings"
    compileSdk = Configs.Sdk.COMPILE

    defaultConfig {
        minSdk = Configs.Sdk.MIN
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = Configs.Java.ENUM_ENTRY
        targetCompatibility = Configs.Java.ENUM_ENTRY
    }

    kotlinOptions {
        jvmTarget = Configs.Java.STRING_VALUE
    }
}

dependencies {
    implementation(project(":domain"))
}