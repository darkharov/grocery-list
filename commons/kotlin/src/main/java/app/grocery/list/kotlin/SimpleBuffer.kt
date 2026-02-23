package app.grocery.list.kotlin

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SimpleBuffer<T> {

    private val items = MutableStateFlow(setOf<T>())

    operator fun plusAssign(item: T) {
        items.update { it + item }
    }

    operator fun minusAssign(item: T) {
        items.update { it - item }
    }

    fun observe(): StateFlow<Set<T>> =
        items.asStateFlow()
}
