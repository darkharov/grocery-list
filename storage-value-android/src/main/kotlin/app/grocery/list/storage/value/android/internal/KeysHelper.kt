package app.grocery.list.storage.value.android.internal

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

internal class KeysHelper(private val prefix: String) {

    fun booleanKey(rawKey: String): Preferences.Key<Boolean> =
        booleanPreferencesKey(rawKey(rawKey))

    fun intKey(rawKey: String): Preferences.Key<Int> =
        intPreferencesKey(rawKey(rawKey))

    fun stringKey(rawKey: String): Preferences.Key<String> =
        stringPreferencesKey(rawKey(rawKey))

    private fun rawKey(key: String) =
        prefix + key
}
