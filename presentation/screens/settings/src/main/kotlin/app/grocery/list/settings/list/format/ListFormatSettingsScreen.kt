package app.grocery.list.settings.list.format

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.grocery.list.commons.compose.elements.AppPreloader
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppTypography
import app.grocery.list.settings.R
import kotlinx.serialization.Serializable

@Serializable
data object ListFormatSettings

internal fun NavGraphBuilder.listFormatSettings() {
    composable<ListFormatSettings> {
        ListFormatSettings()
    }
}

@Composable
private fun ListFormatSettings() {
    val viewModel = hiltViewModel<ListFormatSettingsViewModel>()
    val props by viewModel.props.collectAsState()
    ListFormatSettings(
        props = props,
        callbacks = viewModel,
    )
}

@Composable
private fun ListFormatSettings(
    props: ListFormatSettingsProps?,
    callbacks: ListFormatSettingsCallbacks,
    modifier: Modifier = Modifier,
) {
    if (props == null) {
        AppPreloader(
            modifier = modifier,
        )
    } else {
        Content(
            props = props,
            callbacks = callbacks,
            modifier = modifier,
        )
    }
}

@Composable
private fun Content(
    props: ListFormatSettingsProps,
    callbacks: ListFormatSettingsCallbacks,
    modifier: Modifier = Modifier,
) {
    val horizontalMargin = dimensionResource(R.dimen.margin_16_32_64)
    Column(
        modifier = modifier
            .padding(vertical = 16.dp)
            .fillMaxSize(),
    ) {
        Text(
            text = stringResource(R.string.grocery_list_item),
            style = LocalAppTypography.current.header,
            modifier = Modifier
                .padding(
                    horizontal = horizontalMargin,
                ),
        )
        Spacer(
            modifier = Modifier
                .height(12.dp),
        )
        Column(
            modifier = Modifier
                .selectableGroup(),
        ) {
            for (option in ListFormatSettingsProps.ProductItemFormat.entries) {
                Option(
                    props = props,
                    option = option,
                    horizontalMargin = horizontalMargin,
                    callbacks = callbacks,
                )
            }
        }
    }
}

@Composable
private fun Option(
    props: ListFormatSettingsProps,
    option: ListFormatSettingsProps.ProductItemFormat,
    horizontalMargin: Dp,
    callbacks: ListFormatSettingsCallbacks,
    modifier: Modifier = Modifier,
) {
    val selected = props.productItemFormat == option
    val optionPadding = 8.dp
    val optionHorizontalMargin = horizontalMargin - optionPadding
    Row(
        modifier = modifier
            .padding(horizontal = optionHorizontalMargin)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .selectable(
                selected = selected,
                onClick = {
                    callbacks.onProductListFormatSelected(option)
                },
                role = Role.RadioButton,
            )
            .padding(
                vertical = 12.dp,
                horizontal = optionPadding,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null, // copied from https://developer.android.com/develop/ui/compose/components/radio-button#create-basic
        )
        Text(
            text = stringResource(option.titleId),
            modifier = Modifier
                .padding(start = 8.dp),
        )
    }
}

@Preview
@Composable
private fun ListFormatSettingsScreenPreview() {
    GroceryListTheme {
        Scaffold { padding ->
            ListFormatSettings(
                props = ListFormatSettingsProps(
                    productItemFormat = ListFormatSettingsProps.ProductItemFormat.EmojiAndAdditionalDetail,
                ),
                callbacks = ListFormatSettingsCallbacksMock,
                modifier = Modifier
                    .padding(padding),
            )
        }
    }
}
