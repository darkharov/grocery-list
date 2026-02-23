package app.grocery.list.custom.lists.picker.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.grocery.list.commons.compose.elements.AppHorizontalDivider
import app.grocery.list.commons.compose.elements.AppHorizontalDividerMode
import app.grocery.list.commons.compose.elements.AppRadioButton
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors
import app.grocery.list.commons.compose.theme.LocalAppTypography
import app.grocery.list.custom.lists.R

@Composable
internal fun ProductListPickerItem(
    props: ProductListPickerItemProps,
    callbacks: ProductListPickerItemCallbacks,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(R.dimen.margin_0_16_48),
            )
            .clickable {
                callbacks.onSelect(props)
            }
            .padding(top = 16.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp),
        ) {
            AppRadioButton(
                selected = props.selected,
            )
            Spacer(
                modifier = Modifier
                    .width(16.dp),
            )
            Column {
                Text(
                    text = props.title,
                    color = LocalAppColors.current.blackOrWhite,
                    style = LocalAppTypography.current.header2,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    DemoColors(
                        demoColors = props.demoColors,
                    )
                    Text(
                        text = props.stub ?: stringResource(R.string.product_list_picker_item_empty),
                        color = LocalAppColors.current.blackOrWhite,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .alpha(0.82f),
                    )
                }
            }
        }
        Spacer(
            modifier = Modifier
                .height(4.dp)
        )
        //if (props.hasDivider) {
            AppHorizontalDivider(
                mode = AppHorizontalDividerMode.DividerOnly(useScreenOffset = true),
                modifier = Modifier,
            )
        //}
    }
}

@Composable
private fun DemoColors(
    demoColors: ProductListPickerItemProps.DemoColors,
) {
    val offset = 4.dp
    val cornerRadius = 4.dp
    Spacer(
        modifier = Modifier
            .padding(vertical = 6.dp)
            .size(20.dp)
            .drawBehind {
                drawRoundRect(
                    color = demoColors.first,
                    size = size.copy(
                        width = size.width - offset.toPx(),
                        height = size.height - offset.toPx(),
                    ),
                    cornerRadius = CornerRadius(cornerRadius.toPx()),
                )
                drawRoundRect(
                    color = demoColors.second,
                    topLeft = Offset(offset.toPx(), offset.toPx()),
                    size = size.copy(
                        width = size.width - offset.toPx(),
                        height = size.height - offset.toPx(),
                    ),
                    cornerRadius = CornerRadius(cornerRadius.toPx()),
                )
            },
    )
}

@Composable
@PreviewLightDark
private fun ProductListPickerItemPreview(
    @PreviewParameter(
        provider = ProductListPickerItemMocks::class,
    )
    props: ProductListPickerItemProps,
) {
    GroceryListTheme {
        ProductListPickerItem(
            props = props,
            callbacks = ProductListPickerItemCallbacksMock,
            modifier = Modifier
                .background(
                    LocalAppColors.current.whiteOrBlack,
                ),
        )
    }
}
