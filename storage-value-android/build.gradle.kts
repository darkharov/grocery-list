plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "${Configs.APPLICATION_ID}.storage.value.android"
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
    api(project(":storage-value-kotlin"))
    implementation(libs.javax.inject)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    ksp(libs.kotlinx.metadata.jvm)
}