package app.grocery.list.custom.product.lists.picker.item

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.grocery.list.commons.compose.elements.AppDeleteAction
import app.grocery.list.commons.compose.elements.AppRadioButton
import app.grocery.list.commons.compose.elements.AppSwipeToDismissBox
import app.grocery.list.commons.compose.elements.color.scheme.AppDemoColors
import app.grocery.list.commons.compose.elements.color.scheme.AppDemoColorsSizeProps
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors
import app.grocery.list.custom.product.lists.R

@Composable
internal fun ProductListPickerItem(
    props: ProductListPickerItemProps,
    callbacks: ProductListPickerItemCallbacks,
    modifier: Modifier = Modifier,
) {
    val counter = props.counter
    val fontSizeOfStubAndLabelEmpty = 13.sp
    val shape = RoundedCornerShape(8.dp)
    AppSwipeToDismissBox(
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = props.deletable,
        backgroundContent = { state ->
            Actions(
                state = state,
                modifier = Modifier
                    .clip(shape),
            )
        },
        onDismiss = {
            callbacks.onDelete(props)
        },
        modifier = modifier
            .padding(horizontal = 8.dp + dimensionResource(R.dimen.margin_0_16_48))
            .clip(shape)
            .combinedClickable(
                onClick = {
                    callbacks.onSelect(props)
                },
                onLongClick = {
                    callbacks.onEdit(props)
                },
            ),
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .background(LocalAppColors.current.background)
                .fillMaxWidth()
                .padding(
                    horizontal = 8.dp,
                    vertical = 16.dp,
                )
        ) {
            AppRadioButton(
                selected = props.selected,
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AppDemoColors(
                        size = AppDemoColorsSizeProps.Compact,
                        colorScheme = props.colorScheme,
                    )
                    Spacer(
                        modifier = Modifier
                            .width(8.dp),
                    )
                    Text(
                        text = props.title,
                        color = LocalAppColors.current.blackOrWhite,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(4.dp),
                )
                if (!(props.stub.isNullOrBlank())) {
                    Text(
                        text = props.stub,
                        color = LocalAppColors.current.blackOrWhite,
                        fontSize = fontSizeOfStubAndLabelEmpty,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .alpha(0.82f),
                    )
                } else {
                    Text(
                        text = stringResource(R.string.product_list_picker_item_empty),
                        color = LocalAppColors.current.brand_30_40,
                        fontSize = fontSizeOfStubAndLabelEmpty,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .alpha(counter.alpha),
                    )
                }
            }
            Text(
                text = counter.formattedValue,
                color = LocalAppColors.current.brand_30_40,
                fontSize = 14.sp,
                maxLines = 1,
                modifier = Modifier
                    .alpha(counter.alpha),
            )
        }
    }
}

@Composable
private fun Actions(
    state: SwipeToDismissBoxState,
    modifier: Modifier = Modifier,
) {
    when (state.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> {
            // nothing to show
        }
        SwipeToDismissBoxValue.EndToStart -> {
            AppDeleteAction(modifier)
        }
        SwipeToDismissBoxValue.Settled -> {
            // nothing to show
        }
    }
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
