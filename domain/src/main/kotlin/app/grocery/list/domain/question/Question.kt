package app.grocery.list.domain.question

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class Question {

    abstract fun shouldBeAsked(): Flow<Boolean>

    abstract suspend fun close()

    fun takeIfShouldBeAsked(): Flow<Question?> =
        shouldBeAsked()
            .map { shouldBeAsked ->
                if (shouldBeAsked) {
                    this
                } else {
                    null
                }
            }
}
