package app.grocery.list.storage.value.android

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import javax.inject.Inject

class StorageValueMigrations @Inject constructor(
    private var dataStore: DataStore<Preferences>,
) {
    suspend fun newKeys(
        intKeys: List<Keys> = emptyList(),
        booleanKeys: List<Keys> = emptyList(),
    ) {
        dataStore.edit { prefs ->
            for ((old, new) in intKeys) {
                val oldKey = intPreferencesKey(old)
                val newKey = intPreferencesKey(new)
                val oldValue = prefs[oldKey]
                if (oldValue != null) {
                    prefs[newKey] = oldValue
                    prefs.remove(oldKey)
                }
            }
            for ((old, new) in booleanKeys) {
                val oldKey = booleanPreferencesKey(old)
                val newKey = booleanPreferencesKey(new)
                val oldValue = prefs[oldKey]
                if (oldValue != null) {
                    prefs[newKey] = oldValue
                    prefs.remove(oldKey)
                }
            }
        }
    }

    data class Keys(
        val old: String,
        val new: String,
    )
}
