package app.grocery.list.storage.value.android.value

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import app.grocery.list.storage.value.kotlin.StorageValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal sealed class BaseStorageValue<T> : StorageValue<T> {

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

    final override suspend fun edit(mutation: (T) -> T) {
        dataStore.edit { preferences ->
            write(preferences, mutation(read(preferences)))
        }
    }
}
