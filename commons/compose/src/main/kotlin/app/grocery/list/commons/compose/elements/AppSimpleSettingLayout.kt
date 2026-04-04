package app.grocery.list.commons.compose.elements

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.AppGradientDirection
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.elements.switch_.AppTitledSwitch
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors
import app.grocery.list.commons.compose.values.StringValue
import kotlinx.coroutines.launch

@Composable
fun AppSimpleSettingLayout(
    checked: Boolean,
    @DrawableRes
    imageId: Int,
    text: StringValue,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    explanation: StringValue? = null,
    gradientDirection: AppGradientDirection = AppGradientDirection.Upward,
) {
    val state = rememberScrollState()
    val scrolling = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        scrolling.launch {
            // switch is located at the bottom
            state.animateScrollTo(state.maxValue)
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(R.dimen.margin_16_32_64)),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .verticalScroll(state),
        ) {
            AppExplanationImage(
                imageId = imageId,
                modifier = Modifier
                    .padding(vertical = 32.dp),
                direction = gradientDirection,
            )
            AppTitledSwitch(
                text = text,
                checked = checked,
                onCheckedChange = onCheckedChange,
                explanation = explanation,
                descriptionAlpha = if (checked) 1f else 0f,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun AppSimpleSettingLayoutPreview() {
    GroceryListTheme {
        AppSimpleSettingLayout(
            checked = true,
            imageId = R.drawable.ic_android_200,
            text = StringValue.StringWrapper("Description"),
            explanation = StringValue.StringWrapper("Explanation"),
            onCheckedChange = {},
            modifier = Modifier
                .background(LocalAppColors.current.background),
        )
    }
}
