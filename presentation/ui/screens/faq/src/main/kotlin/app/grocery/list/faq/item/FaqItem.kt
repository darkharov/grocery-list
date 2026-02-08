package app.grocery.list.faq.item

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.theme.AppIcons
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppTypography
import app.grocery.list.faq.R

private val shape = RoundedCornerShape(16.dp)

@Composable
internal fun FaqItem(
    props: FaqItemProps,
    callbacks: FaqItemCallbacks,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(
                horizontal = dimensionResource(R.dimen.margin_16_32_64),
                vertical = 8.dp,
            )
            .border(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = shape,
            )
            .clip(shape)
            .clickable(
                interactionSource = null,
                indication = null,
            ) {
                callbacks.onFaqItemClick(
                    id = props.id,
                    expanded = !(props.expanded),
                )
            }
            .padding(16.dp)
            .fillMaxWidth(),
    ) {
        Question(props)
        Answer(props)
    }
}

@Composable
private fun Question(
    props: FaqItemProps,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = props.question,
            style = LocalAppTypography.current.header2,
            modifier = Modifier
                .weight(1f),
        )
        Spacer(
            modifier = Modifier
                .width(8.dp),
        )
        val degrees by animateFloatAsState(
            targetValue = if (props.expanded) 180f else 0f,
        )
        Icon(
            painter = rememberVectorPainter(AppIcons.expandMore),
            contentDescription = null,
            modifier = Modifier
                .requiredSize(24.dp)
                .rotate(degrees)
        )
    }
}

@Composable
private fun Answer(
    props: FaqItemProps,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = props.expanded,
        enter = fadeIn() + expandVertically(),
        exit = shrinkVertically() + fadeOut(),
        modifier = modifier,
    ) {
        Text(
            text = props.answer,
            modifier = Modifier
                .padding(top = 12.dp),
        )
    }
}

@Composable
@Preview
private fun FaqItemPreview(
    @PreviewParameter(
        provider = FaqItemMocks::class,
    )
    props: FaqItemProps,
) {
    GroceryListTheme {
        var props by remember { mutableStateOf(props) }
        FaqItem(
            props = props,
            callbacks = object : FaqItemCallbacks {
                override fun onFaqItemClick(id: Int, expanded: Boolean) {
                    props = props.copy(expanded = expanded)
                }
            },
            modifier = Modifier,
        )
    }
}
