plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "${Configs.APPLICATION_ID}.product.list.actions"
    compileSdk = Configs.Sdk.COMPILE

    defaultConfig {
        minSdk = Configs.Sdk.MIN
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
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":commons:compose"))
    implementation(project(":commons:resources"))
    implementation(project(":commons:android"))
    implementation(project(":presentation:sharing"))
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.core.ktx)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    ksp(libs.kotlinx.metadata.jvm)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
}
