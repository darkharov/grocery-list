package app.grocery.list.storage.value.kotlin

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

interface StorageValue<T> {
    suspend fun set(newValue: T)
    suspend fun edit(mutation: (T) -> T)
    fun observe(): Flow<T>
}

suspend fun <T> StorageValue<T>.get(): T =
    observe().first()

suspend fun StorageValue<Int>.inc() {
    this.edit { it + 1 }
}

suspend fun StorageValue<Int>.dec() {
    this.edit { it + 1 }
}
