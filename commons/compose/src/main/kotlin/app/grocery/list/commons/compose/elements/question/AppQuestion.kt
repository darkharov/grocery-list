package app.grocery.list.commons.compose.elements.question

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.elements.button.icon.AppCloseButton
import app.grocery.list.commons.compose.elements.button.icon.AppIconButtonSize
import app.grocery.list.commons.compose.elements.button.text.AppUnderlinedTextButton
import app.grocery.list.commons.compose.theme.GroceryListTheme

private const val CONTENT_TYPE = "app.grocery.list.commons.compose.elements.question.AppQuestion"

@Composable
fun AppQuestion(
    props: AppQuestionProps,
    callbacks: AppQuestionCallbacks,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .widthIn(max = 420.dp)
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(R.dimen.margin_16_32_64),
            ),
    ) {
        val closeButtonType = AppIconButtonSize.Normal
        AppUnderlinedTextButton(
            text = props.text,
            onClick = {
                callbacks.onQuestionClick(props)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = closeButtonType.size)
        )
        AppCloseButton(
            onClick = {
                callbacks.onQuestionClose(props)
            },
            type = closeButtonType,
            modifier = Modifier
                .align(Alignment.CenterEnd),
        )
    }
}

fun LazyListScope.optionalAppQuestion(
    props: AppQuestionProps?,
    callbacks: AppQuestionCallbacks,
    modifier: Modifier = Modifier,
) {
    if (props != null) {
        item(
            key = props.key,
            contentType = CONTENT_TYPE,
        ) {
            AppQuestion(
                props = props,
                callbacks = callbacks,
                modifier = modifier
                    .animateItem(),
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun AppQuestionPreview(
    @PreviewParameter(
        provider = AppQuestionMocks::class,
    )
    props: AppQuestionProps,
) {
    GroceryListTheme {
        AppQuestion(
            props = props,
            callbacks = AppQuestionCallbacksMock,
            modifier = Modifier,
        )
    }
}
