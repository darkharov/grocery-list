package app.grocery.list.domain.question

import kotlinx.coroutines.flow.Flow

abstract class Question {
    abstract fun shouldBeAsked(): Flow<Boolean>
}
