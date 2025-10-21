import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.kotlin.serialization)
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
        versionCode = 33
        versionName = "1.16.0"
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
    kotlinOptions {
        jvmTarget = Configs.Java.STRING_VALUE
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":commons:resources"))
    implementation(project(":commons:compose"))
    implementation(project(":commons:android"))
    implementation(project(":commons:format"))
    implementation(project(":presentation:notifications"))
    implementation(project(":presentation:screens:product-input-form"))
    implementation(project(":presentation:screens:product-list-preview"))
    implementation(project(":presentation:screens:product-list-actions"))
    implementation(project(":presentation:screens:clear-notifications-reminder"))
    implementation(project(":presentation:screens:final-steps"))
    implementation(project(":presentation:screens:settings"))
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
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
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.kotlinx.collections.immutable)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics.ndk)
}
