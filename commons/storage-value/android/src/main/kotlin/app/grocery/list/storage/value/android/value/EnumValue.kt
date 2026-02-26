package app.grocery.list.storage.value.android.value

import app.grocery.list.storage.value.kotlin.StorageValue
import kotlin.enums.EnumEntries
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class EnumValue<T : Enum<T>>(
    private val backing: StorageValue<Int>,
    private val entries: EnumEntries<T>,
) : StorageValue<T> {

    override suspend fun set(newValue: T) {
        backing.set(newValue.ordinal)
    }

    override suspend fun edit(mutation: (T) -> T) {
        backing.edit { currentOrdinal ->
            val currentValue = entries[currentOrdinal]
            val newValue = mutation(currentValue)
            newValue.ordinal
        }
    }

    override fun observe(): Flow<T> =
        backing.observe().map { entries[it] }
}
