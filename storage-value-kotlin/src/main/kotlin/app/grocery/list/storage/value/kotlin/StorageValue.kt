package app.grocery.list.storage.value.kotlin

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

interface StorageValue<T> {
    suspend fun set(newValue: T)
    fun observe(): Flow<T>
}

suspend fun <T> StorageValue<T>.get(): T =
    observe().first()

suspend fun <T> StorageValue<T>.edit(mutation: (T) -> T): T {
    val value = mutation(get())
    set(value)
    return value
}
