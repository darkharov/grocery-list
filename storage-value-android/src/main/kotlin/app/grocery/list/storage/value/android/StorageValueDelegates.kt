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
import kotlin.properties.ReadOnlyProperty

@Singleton
class StorageValueDelegates @Inject internal constructor(
    private val factory: PreferencesStorageValue.DelegatesFactory,
) {
    fun int(defaultValue: Int = 0): ReadOnlyProperty<Any, StorageValue<Int>> =
        factory.primitiveValue(
            defaultValue = defaultValue,
            makeKey = ::intPreferencesKey
        )

    fun boolean(defaultValue: Boolean = false): ReadOnlyProperty<Any, StorageValue<Boolean>> =
        factory.primitiveValue(
            defaultValue = defaultValue,
            makeKey = ::booleanPreferencesKey
        )

    inline fun <reified E : Enum<E>> enum(defaultValue: E): ReadOnlyProperty<Any, StorageValue<E>> {
        val key = "ordinal"
        return custom(
            write = { int(key, it.ordinal) },
            read = { enumValues<E>()[int(key, defaultValue = defaultValue.ordinal)] }
        )
    }

    fun string(defaultValue: String = ""): ReadOnlyProperty<Any, StorageValue<String>> =
        factory.primitiveValue(
            defaultValue = defaultValue,
            makeKey = ::stringPreferencesKey
        )

    fun <T> custom(
        write: Writer.(T) -> Unit,
        read: Reader.() -> T,
    ): ReadOnlyProperty<Any, StorageValue<T>> =
        factory.custom(
            write = write,
            read = read
        )
}
