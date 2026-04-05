package app.grocery.list.commons.compose.elements.question

import androidx.compose.runtime.Stable

@Stable
interface AppQuestionCallbacks {
    fun onQuestionClose(question: AppQuestionProps)
    fun onQuestionClick(question: AppQuestionProps)
}

object AppQuestionCallbacksMock : AppQuestionCallbacks {
    override fun onQuestionClose(question: AppQuestionProps) {}
    override fun onQuestionClick(question: AppQuestionProps) {}
}
