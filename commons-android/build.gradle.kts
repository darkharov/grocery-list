plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "commons.android"   // It is a cross-project module, so without ${Configs.APPLICATION_ID} prefix
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
    implementation(libs.material)
    implementation(libs.javax.inject)
    implementation(libs.androidx.core.splashscreen)
}
