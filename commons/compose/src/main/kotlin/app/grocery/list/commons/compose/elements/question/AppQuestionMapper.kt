package app.grocery.list.commons.compose.elements.question

import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.domain.question.HowToEditCustomListsQuestion
import app.grocery.list.domain.question.HowToEditProductsQuestion
import app.grocery.list.domain.question.NeedMoreListsQuestion
import app.grocery.list.domain.question.Question
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppQuestionMapper @Inject constructor() {

    fun toPresentationNullable(question: Question?): AppQuestionProps? =
        if (question != null) {
            toPresentation(question)
        } else {
            null
        }

    fun toPresentation(question: Question): AppQuestionProps {
        val (key, text) = when (question) {
            is NeedMoreListsQuestion -> {
                "NeedMoreListsQuestion" to StringValue.ResId(R.string.need_more_lists)
            }
            is HowToEditCustomListsQuestion -> {
                "HowToEditCustomListsQuestion" to StringValue.ResId(R.string.how_to_edit_items)
            }
            is HowToEditProductsQuestion -> {
                "HowToEditProductsQuestion" to StringValue.ResId(R.string.how_to_edit_items)
            }
        }
        return AppQuestionProps(
            key = key,
            text = text,
            payload = question,
        )
    }

    fun toDomain(props: AppQuestionProps): Question =
        props.payload as Question
}
