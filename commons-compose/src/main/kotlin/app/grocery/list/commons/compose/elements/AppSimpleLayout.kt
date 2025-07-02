package app.grocery.list.commons.compose.elements

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.theme.GroceryListTheme

@Composable
fun AppSimpleLayout(
    modifier: Modifier = Modifier,
    scrollableContent: @Composable ColumnScope.() -> Unit,
    footer: @Composable (() -> Unit)? = null,
) {
    val scrollState = rememberScrollState()
    Box(
        modifier = modifier,
    ) {
        Content(
            scrollState = scrollState,
            scrollableContent = scrollableContent,
            footer = footer,
        )
        if (scrollState.canScrollBackward) {
            AppHorizontalDivider(
                mode = AppHorizontalDividerMode.Shadow.Downward,
            )
        }
    }
}

@Composable
private fun Content(
    scrollState: ScrollState,
    scrollableContent: @Composable ColumnScope.() -> Unit,
    footer: @Composable (() -> Unit)?,
) {
    val horizontal = dimensionResource(R.dimen.margin_16_32_64)
    Column {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = horizontal,
                )
                .weight(1f)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            scrollableContent()
        }
        if (footer != null) {
            Box(
                modifier = Modifier
                    .padding(horizontal = horizontal),
            ) {
                footer()
            }
        }
        NavBarOffset()
    }
}

@Composable
private fun NavBarOffset() {
    Spacer(
        modifier = Modifier
            .windowInsetsBottomHeight(WindowInsets.navigationBars)
            .fillMaxWidth(),
    )
}

@PreviewLightDark
@Preview(device = "spec:width=1280dp,height=800dp,dpi=240,orientation=portrait")
@Composable
private fun AppSimpleLayoutPreview() {
    GroceryListTheme {
        Scaffold { padding ->
            AppSimpleLayout(
                modifier = Modifier
                    .padding(padding),
                scrollableContent = {
                    Text(text = LoremIpsum)
                },
                footer = {
                    Text(
                        text = "Footer",
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(24.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                },
            )
        }
    }
}

private const val LoremIpsum = """
Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed dictum condimentum nibh, in dignissim quam pellentesque in. Phasellus eget vehicula velit. Fusce suscipit scelerisque pretium. Ut at enim augue. Quisque ut suscipit quam. Mauris et massa id magna sollicitudin laoreet bibendum non turpis. Interdum et malesuada fames ac ante ipsum primis in faucibus. Nunc faucibus accumsan justo sed facilisis. Etiam pharetra ipsum et laoreet posuere. Quisque efficitur diam sed leo molestie consectetur. Mauris vel odio venenatis, sollicitudin ligula at, consectetur magna. Nam id iaculis nunc.

Curabitur eget quam nec turpis hendrerit semper. Suspendisse sit amet dui purus. Sed pellentesque vitae nibh id convallis. Pellentesque congue, mauris at elementum commodo, nunc ex scelerisque magna, non suscipit risus metus vitae sem. Proin ac velit elit. Interdum et malesuada fames ac ante ipsum primis in faucibus. Integer posuere fringilla velit eu tincidunt. Donec ornare quam ut sapien vulputate, sed gravida diam convallis. Maecenas vel malesuada sapien, in molestie erat. Phasellus et lectus dictum arcu condimentum viverra a eu urna. Pellentesque sagittis sapien hendrerit lorem consequat, eget laoreet lorem sodales. Donec pharetra at est et ullamcorper. Aliquam vitae finibus neque. Duis porttitor at felis nec vulputate. Aliquam molestie, diam ut faucibus aliquet, ligula ligula tempus libero, ac tincidunt dolor erat a urna.

Sed cursus orci nec magna blandit condimentum. Etiam fringilla sapien at aliquam placerat. Donec maximus tincidunt consequat. In hac habitasse platea dictumst. Integer suscipit vehicula lacus, eget aliquam enim condimentum in. Donec pretium egestas ligula ut fringilla. Nullam vestibulum iaculis risus lacinia malesuada. Maecenas pellentesque est metus, sit amet tempus est fringilla id. Nulla finibus malesuada lectus sed scelerisque.

Curabitur ultrices elementum facilisis. Sed a viverra purus. Donec sed lectus in massa aliquet commodo sit amet eget dolor. Pellentesque et dictum sem. Quisque id turpis nisl. Nulla commodo erat ac blandit tempor. Nam euismod, sapien vitae egestas convallis, nunc libero laoreet leo, auctor porttitor est lorem quis ligula. Morbi in vehicula turpis.

Donec ligula arcu, feugiat in odio ac, venenatis lobortis diam. Aenean eu lectus pharetra, semper leo ut, venenatis nisl. Curabitur hendrerit turpis id est porta euismod. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Pellentesque congue fermentum libero vel varius. Sed id interdum risus. Maecenas porta turpis ut odio porta, a mattis nibh condimentum. Proin ornare sed metus sed consectetur. Nulla posuere est a lectus mattis consequat ut ut ipsum. Praesent dignissim arcu sit amet massa sollicitudin laoreet nec eu mauris. Suspendisse potenti. Nulla ullamcorper elementum sapien, vitae elementum nunc vulputate vel. Proin sed augue eu tortor malesuada posuere sed a ligula.
"""
