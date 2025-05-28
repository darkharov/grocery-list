import org.gradle.api.JavaVersion

object Configs {

    const val APPLICATION_ID = "app.grocery.list"

    const val SERIALIZATION = "2.0.21"

    object Sdk {
        const val MIN = 26
        const val COMPILE = 35
        const val TARGET = COMPILE
    }

    object Java {
        val ENUM_ENTRY = JavaVersion.VERSION_17
        val JVM_TARGET = "JVM_17"
        const val STRING_VALUE = "17"
    }
}
