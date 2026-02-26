package app.grocery.list.storage.value.android.value

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences

internal class PrimitiveValue<T>(
    override val dataStore: DataStore<Preferences>,
    private val key: Preferences.Key<T>,
    private val defaultValue: T,
) : BaseStorageValue<T>() {

    override fun read(preferences: Preferences): T =
        preferences[key] ?: defaultValue

    override fun write(preferences: MutablePreferences, value: T) {
        preferences[key] = value
    }
}
