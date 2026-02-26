package app.grocery.list.storage.value.android

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import app.grocery.list.storage.value.android.value.EnumValue
import app.grocery.list.storage.value.android.value.NullablePrimitiveValue
import app.grocery.list.storage.value.android.value.PrimitiveValue
import app.grocery.list.storage.value.kotlin.StorageValue
import javax.inject.Inject
import kotlin.enums.EnumEntries
import kotlin.enums.enumEntries

class StorageValueFactory @Inject internal constructor(
    private var dataStore: DataStore<Preferences>,
) {
    fun int(
        key: String,
        defaultValue: Int = 0,
    ): StorageValue<Int> =
        PrimitiveValue(
            key = intPreferencesKey(key),
            dataStore = dataStore,
            defaultValue = defaultValue
        )

    fun nullableInt(
        key: String,
        defaultValue: Int? = null,
    ): StorageValue<Int?> =
        NullablePrimitiveValue(
            dataStore = dataStore,
            defaultValue = defaultValue,
            key = intPreferencesKey(key),
        )

    fun boolean(
        key: String,
        defaultValue: Boolean = false,
    ): StorageValue<Boolean> =
        PrimitiveValue(
            dataStore = dataStore,
            defaultValue = defaultValue,
            key = booleanPreferencesKey(key),
        )

    inline fun <reified E : Enum<E>> enum(
        key: String,
        defaultValue: E,
    ): StorageValue<E> =
        enum(
            key = key,
            defaultValue = defaultValue,
            entries = enumEntries(),
        )

    fun <E : Enum<E>> enum(
        key: String,
        defaultValue: E,
        entries: EnumEntries<E>,
    ): StorageValue<E> =
        EnumValue(
            backing = PrimitiveValue(
                dataStore = dataStore,
                key = intPreferencesKey(key),
                defaultValue = defaultValue.ordinal,
            ),
            entries = entries,
        )

    fun string(
        key: String,
        defaultValue: String = "",
    ): StorageValue<String> =
        PrimitiveValue(
            dataStore = dataStore,
            defaultValue = defaultValue,
            key = stringPreferencesKey(key),
        )
}
