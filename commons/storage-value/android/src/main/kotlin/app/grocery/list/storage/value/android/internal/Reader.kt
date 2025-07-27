package app.grocery.list.storage.value.android.internal

import androidx.datastore.preferences.core.Preferences

class Reader internal constructor(
    private val keysHelper: KeysHelper,
    private val preferences: Preferences,
) {
    fun boolean(rawKey: String, defaultValue: Boolean = false) =
        preferences[keysHelper.booleanKey(rawKey)] ?: defaultValue

    fun int(rawKey: String, defaultValue: Int = 0): Int =
        preferences[keysHelper.intKey(rawKey)] ?: defaultValue

    fun string(rawKey: String, defaultValue: String? = null) =
        preferences[keysHelper.stringKey(rawKey)] ?: defaultValue
}
