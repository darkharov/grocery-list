import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = Configs.Java.ENUM_ENTRY
    targetCompatibility = Configs.Java.ENUM_ENTRY
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.valueOf(Configs.Java.JVM_TARGET)
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
