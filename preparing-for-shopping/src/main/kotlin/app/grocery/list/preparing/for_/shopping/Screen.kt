package app.grocery.list.preparing.for_.shopping

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.grocery.list.commons.compose.drawUpwardGradient
import app.grocery.list.commons.compose.elements.AppHorizontalDivider
import app.grocery.list.commons.compose.elements.AppHorizontalDividerMode
import app.grocery.list.commons.compose.theme.GroceryListTheme
import kotlin.math.min

const val PreparingForShopping = "PreparingForShopping"

fun NavGraphBuilder.preparingForShopping() {
    composable(PreparingForShopping) {
        PreparingForShopping()
    }
}

@Composable
private fun PreparingForShopping(
    modifier: Modifier = Modifier,
) {
    val horizontal = dimensionResource(R.dimen.margin_16_32_64)
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier,
    ) {
        if (scrollState.canScrollBackward) {
            AppHorizontalDivider(
                mode = AppHorizontalDividerMode.Shadow.Downward,
                modifier = Modifier
                    .align(Alignment.TopCenter),
            )
        }
        Column(
            modifier = modifier
                .padding(
                    horizontal = horizontal,
                )
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            StepsToTake()
            Spacer(
                modifier = Modifier
                    .height(24.dp),
            )
            val width = with(LocalDensity.current) {
                with(LocalWindowInfo.current.containerSize) {
                    min(width, height).toDp()
                }
            }
            Image(
                painter = painterResource(R.drawable.lock_the_screen),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    MaterialTheme.colorScheme.primary,
                ),
                modifier = Modifier
                    .width(width)
                    .drawUpwardGradient(MaterialTheme.colorScheme.background),
            )
            Spacer(
                modifier = Modifier
                    .windowInsetsBottomHeight(WindowInsets.navigationBars)
                    .fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun StepsToTake() {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            val items = stringArrayResource(R.array.preparing_for_shopping_steps)
            for (item in items) {
                Row {
                    Text(
                        text = "\u2022 ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = item,
                        fontSize = 16.sp,
                        letterSpacing = 0.5.sp,
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PreparingForShoppingPreview() {
    GroceryListTheme {
        Scaffold { padding ->
            PreparingForShopping(
                modifier = Modifier
                    .padding(padding),
            )
        }
    }
}
