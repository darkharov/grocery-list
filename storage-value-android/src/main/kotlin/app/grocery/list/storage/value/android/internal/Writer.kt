package app.grocery.list.storage.value.android.internal

import androidx.datastore.preferences.core.MutablePreferences

class Writer internal constructor(
    private val keysHelper: KeysHelper,
    private val preferences: MutablePreferences,
) {
    fun boolean(rawKey: String, value: Boolean) {
        preferences[keysHelper.booleanKey(rawKey)] = value
    }

    fun int(rawKey: String, value: Int) {
        preferences[keysHelper.intKey(rawKey)] = value
    }

    fun string(rawKey: String, value: String?) {
        val key = keysHelper.stringKey(rawKey)
        if (value == null) {
            preferences.remove(key)
        } else {
            preferences[key] = value
        }
    }
}
