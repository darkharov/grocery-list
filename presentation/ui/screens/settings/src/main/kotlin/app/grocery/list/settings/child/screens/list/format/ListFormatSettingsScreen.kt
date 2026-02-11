package app.grocery.list.settings.child.screens.list.format

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import app.grocery.list.commons.compose.elements.AppPreloader
import app.grocery.list.commons.compose.elements.ScrollableContentWithShadows
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppTypography
import app.grocery.list.settings.R

@Composable
fun ListFormatSettingsScreen() {
    val viewModel = hiltViewModel<ListFormatSettingsViewModel>()
    val props by viewModel.props.collectAsState()
    ListFormatSettingsScreen(
        props = props,
        callbacks = viewModel,
    )
}

@Composable
private fun ListFormatSettingsScreen(
    props: ListFormatSettingsProps?,
    callbacks: ListFormatSettingsCallbacks,
    modifier: Modifier = Modifier,
) {
    if (props == null) {
        AppPreloader(
            modifier = modifier,
        )
    } else {
        val scrollableState = rememberScrollState()
        ScrollableContentWithShadows(
            scrollableState = scrollableState,
            modifier = modifier
                .fillMaxSize(),
        ) {
            Content(
                props = props,
                callbacks = callbacks,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollableState)
                    .windowInsetsPadding(WindowInsets.navigationBars),
            )
        }
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
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.list_item_in_notification),
            style = LocalAppTypography.current.header,
            modifier = Modifier
                .padding(
                    start = horizontalMargin,
                    end = horizontalMargin,
                    top = 32.dp,
                    bottom = 20.dp,
                ),
        )
        Column(
            modifier = Modifier
                .selectableGroup(),
        ) {
            for (option in ListFormatSettingsProps.ProductTitleFormat.entries) {
                Option(
                    props = props,
                    option = option,
                    horizontalMargin = horizontalMargin,
                    callbacks = callbacks,
                )
            }
        }
        Text(
            text = stringResource(R.string.example_of_notification),
            modifier = Modifier
                .padding(
                    horizontal = horizontalMargin,
                )
                .padding(
                    top = 28.dp,
                    bottom = 8.dp,
                ),
            style = LocalAppTypography.current.label,
        )
        SampleNotification(
            title = props.sampleOfNotificationTitle,
        )
        Text(
            text = stringResource(R.string.example_of_notification_warning),
            modifier = Modifier
                .padding(
                    vertical = 8.dp,
                    horizontal = horizontalMargin + 16.dp,
                ),
            textAlign = TextAlign.Center,
            style = LocalAppTypography.current.explanation,
        )
    }
}

@Composable
private fun Option(
    props: ListFormatSettingsProps,
    option: ListFormatSettingsProps.ProductTitleFormat,
    horizontalMargin: Dp,
    callbacks: ListFormatSettingsCallbacks,
    modifier: Modifier = Modifier,
) {
    val selected = props.productTitleFormat == option
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
                    callbacks.onProductTitleFormatSelected(option)
                },
                role = Role.RadioButton,
            )
            .padding(
                vertical = 8.dp,
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

@Composable
private fun SampleNotification(
    title: String,
    modifier: Modifier = Modifier,
) {
    val borderWidth = 2.dp
    val color = MaterialTheme.colorScheme.primary
    val innerPadding = 12.dp
    Row(
        modifier = modifier
            .padding(
                horizontal = dimensionResource(R.dimen.margin_16_32_64),
            )
            .border(
                width = borderWidth,
                color = color,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(innerPadding)
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(innerPadding),
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_notification_preview),
            colorFilter = ColorFilter.tint(color),
            contentScale = ContentScale.Fit,
            contentDescription = null,
            modifier = Modifier
                .border(
                    width = borderWidth,
                    color = color,
                    shape = CircleShape,
                )
                .width(28.dp)
                .padding(5.dp)
                .aspectRatio(1f),
        )
        Text(
            text = title,
            fontSize = 14.sp,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier,
        )
    }
}

@PreviewLightDark
@Composable
private fun ListFormatSettingsScreenPreview() {
    GroceryListTheme {
        Scaffold { padding ->
            ListFormatSettingsScreen(
                props = ListFormatSettingsProps(
                    productTitleFormat = ListFormatSettingsProps.ProductTitleFormat.EmojiAndFullText,
                    sampleOfNotificationTitle = "🍅 Tomato (cherry, 1 container), 🧀 Cheese 500g, 🥛  Milk 2l",
                ),
                callbacks = ListFormatSettingsCallbacksMock,
                modifier = Modifier
                    .padding(padding),
            )
        }
    }
}
