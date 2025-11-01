package app.grocery.list.storage.value.android

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import app.grocery.list.storage.value.android.internal.PreferencesStorageValue
import app.grocery.list.storage.value.android.internal.Reader
import app.grocery.list.storage.value.android.internal.Writer
import app.grocery.list.storage.value.kotlin.StorageValue
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageValueFactory @Inject internal constructor(
    private val factory: PreferencesStorageValue.DelegatesFactory,
) {
    fun int(
        defaultValue: Int = 0,
        key: String,
    ): StorageValue<Int> =
        factory.primitiveValue(
            defaultValue = defaultValue,
            key = intPreferencesKey(key),
        )

    fun boolean(
        defaultValue: Boolean = false,
        key: String,
    ): StorageValue<Boolean> =
        factory.primitiveValue(
            defaultValue = defaultValue,
            key = booleanPreferencesKey(key),
        )

    inline fun <reified E : Enum<E>> enum(
        defaultValue: E,
        keyPrefix: String,
    ): StorageValue<E> {
        val key = "ordinal"
        return custom(
            write = { int(key, it.ordinal) },
            read = { enumValues<E>()[int(key, defaultValue = defaultValue.ordinal)] },
            keyPrefix = keyPrefix,
        )
    }

    fun string(
        defaultValue: String = "",
        key: String,
    ): StorageValue<String> =
        factory.primitiveValue(
            defaultValue = defaultValue,
            key = stringPreferencesKey(key),
        )

    fun <T> custom(
        write: Writer.(T) -> Unit,
        read: Reader.() -> T,
        keyPrefix: String,
    ): StorageValue<T> =
        factory.custom(
            write = write,
            read = read,
            keyPrefix = keyPrefix,
        )
}
