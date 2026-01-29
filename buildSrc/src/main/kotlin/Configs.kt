import org.gradle.api.JavaVersion

object Configs {

    const val APPLICATION_ID = "app.grocery.list"

    object Sdk {
        const val MIN = 26
        const val COMPILE = 36
        const val TARGET = COMPILE
    }

    object Java {
        val ENUM_ENTRY = JavaVersion.VERSION_17
        const val JVM_TARGET = "JVM_17"
    }
}
