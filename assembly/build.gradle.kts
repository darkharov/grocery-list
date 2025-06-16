import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    kotlin("plugin.serialization") version Configs.SERIALIZATION
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
        versionCode = 1
        versionName = "1.0"
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
        }
        debug {
            signingConfig = signingConfigs.getByName("customDebug")
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
    implementation(project(":data"))
    implementation(project(":notifications"))
    implementation(project(":strings"))
    implementation(project(":commons-compose"))
    implementation(project(":commons-android"))
    implementation(project(":product-input-form"))
    implementation(project(":product-list-preview"))
    implementation(project(":product-list-actions"))
    implementation(project(":preparing-for-shopping"))
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.hilt.navigation.compose)
}
