package app.grocery.list.storage.value.android.internal

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import app.grocery.list.storage.value.kotlin.StorageValue
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

internal sealed class PreferencesStorageValue<T> : StorageValue<T> {

    protected abstract val dataStore: DataStore<Preferences>
    protected abstract fun read(preferences: Preferences): T
    protected abstract fun write(preferences: MutablePreferences, value: T)

    final override suspend fun set(newValue: T) {
        dataStore.edit { preferences ->
            write(preferences, newValue)
        }
    }

    final override fun observe(): Flow<T> =
        dataStore
            .data
            .map { preferences -> read(preferences) }
            .distinctUntilChanged()


    private class CustomValue<T>(
        override val dataStore: DataStore<Preferences>,
        private val keysHelper: KeysHelper,
        private val write: Writer.(value: T) -> Unit,
        private val read: Reader.() -> T,
    ) : PreferencesStorageValue<T>() {

        override fun read(preferences: Preferences): T {
            val reader = Reader(keysHelper = keysHelper, preferences = preferences)
            return reader.read()
        }

        override fun write(preferences: MutablePreferences, value: T) {
            val writer = Writer(keysHelper = keysHelper, preferences = preferences)
            writer.write(value)
        }
    }


    private class PrimitiveValue<T>(
        override val dataStore: DataStore<Preferences>,
        private val key: Preferences.Key<T>,
        private val defaultValue: T,
    ) : PreferencesStorageValue<T>() {

        override fun read(preferences: Preferences): T =
            preferences[key] ?: defaultValue

        override fun write(preferences: MutablePreferences, value: T) {
            preferences[key] = value
        }
    }


    @Singleton
    class DelegatesFactory @Inject constructor(
        private var dataStore: DataStore<Preferences>,
    ) {
        fun <T : Any> primitiveValue(
            defaultValue: T,
            key: Preferences.Key<T>,
        ): StorageValue<T> =
            PrimitiveValue(
                key = key,
                dataStore = dataStore,
                defaultValue = defaultValue
            )

        fun <T> custom(
            write: Writer.(T) -> Unit,
            read: Reader.() -> T,
            keyPrefix: String,
        ): StorageValue<T> =
            CustomValue(
                dataStore = dataStore,
                keysHelper = KeysHelper(prefix = keyPrefix),
                write = write,
                read = read,
            )
    }
}
