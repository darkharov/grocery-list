import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.google.hilt.android)
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

val keystorePropertiesFile = rootProject.file("keystore-release.properties")
val keystoreProperties: Properties? =
    try {
        val temp = Properties()
        temp.load(FileInputStream(keystorePropertiesFile))
        temp
    } catch (ignored: Exception) {
        // guest mode
        null
    }

android {
    namespace = "${Configs.APPLICATION_ID}.assembly"
    compileSdk = Configs.Sdk.COMPILE

    defaultConfig {
        applicationId = Configs.APPLICATION_ID
        minSdk = Configs.Sdk.MIN
        targetSdk = Configs.Sdk.TARGET
        versionCode = 51
        versionName = "1.19.3"
    }

    signingConfigs {
        create("release") {
            if (keystoreProperties != null) {
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
            }
        }
        create("customDebug") {
            keyAlias = "grocery-list"
            keyPassword = "123456"
            storeFile = file("../keystore-debug.jks")
            storePassword = "123456"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
            configure<CrashlyticsExtension> {
                mappingFileUploadEnabled = true
            }
        }
        debug {
            signingConfig = signingConfigs.getByName("customDebug")
        }
    }
    compileOptions {
        sourceCompatibility = Configs.Java.ENUM_ENTRY
        targetCompatibility = Configs.Java.ENUM_ENTRY
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":presentation:ui:main-activity"))
    implementation(project(":commons:resources"))
    implementation(project(":commons:compose"))
    implementation(project(":commons:kotlin"))
    implementation(project(":commons:android"))
    implementation(project(":presentation:notifications"))
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
