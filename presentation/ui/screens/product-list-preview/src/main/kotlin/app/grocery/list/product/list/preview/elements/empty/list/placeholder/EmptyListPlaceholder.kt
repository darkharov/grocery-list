package app.grocery.list.product.list.preview.elements.empty.list.placeholder

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors
import app.grocery.list.commons.compose.theme.LocalAppTypography
import app.grocery.list.commons.compose.values.value
import app.grocery.list.product.list.preview.R

@Composable
internal fun EmptyListPlaceholder(
    props: EmptyListPlaceholderProps,
    callbacks: EmptyListPlaceholderCallbacks,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(
                horizontal = 16.dp + dimensionResource(R.dimen.margin_16_32_64),
            ),
    ) {
        val startPadding = 8.dp
        Text(
            text = props.text.value(),
            color = LocalAppColors.current.blackOrWhite,
            style = LocalAppTypography.current.label,
            modifier = Modifier
                .padding(start = startPadding),
        )
        Spacer(
            modifier = Modifier
                .height(16.dp),
        )
        val templates = props.templates
        if (templates != null) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                for (template in templates) {
                    Text(
                        text = "+ ${template.title}",
                        color = LocalAppColors.current.brand_40_40,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                callbacks.onTemplateClick(template)
                            }
                            .padding(vertical = 6.dp)
                            .padding(
                                end = 12.dp,
                                start = startPadding,
                            ),
                    )
                }
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun EmptyListPlaceholderPreview(
    @PreviewParameter(
        provider = EmptyListPlaceholderMocks::class,
    )
    props: EmptyListPlaceholderProps,
) {
    GroceryListTheme {
        EmptyListPlaceholder(
            props = props,
            callbacks = EmptyListPlaceholderCallbacksMock,
            modifier = Modifier
                .background(LocalAppColors.current.background),
        )
    }
}
