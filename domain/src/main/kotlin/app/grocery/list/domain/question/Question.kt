package app.grocery.list.domain.question

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

sealed class Question {

    abstract fun shouldBeAsked(): Flow<Boolean>
    abstract suspend fun close()

    fun takeIfShouldBeAsked(): Flow<Question?> =
        takeIfShouldBeAskedOr(alternative = null)

    @OptIn(ExperimentalCoroutinesApi::class)
    fun takeIfShouldBeAskedOr(alternative: (() -> Question)? = null): Flow<Question?> =
        shouldBeAsked()
            .flatMapLatest { shouldBeAsked ->
                when {
                    shouldBeAsked -> {
                        flowOf(this)
                    }
                    (alternative != null) -> {
                        alternative().takeIfShouldBeAsked()
                    }
                    else -> {
                        flowOf(null)
                    }
                }
            }
}
