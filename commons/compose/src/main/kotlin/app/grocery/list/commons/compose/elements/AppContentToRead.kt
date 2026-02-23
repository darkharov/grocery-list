package app.grocery.list.commons.compose.elements

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors
import app.grocery.list.commons.compose.theme.LocalAppTypography

@Composable
fun AppContentToRead(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable ColumnScope.() -> Unit,
    @DrawableRes
    imageId: Int,
    footer: @Composable (() -> Unit)? = null,
) {
    Box(
        modifier = modifier,
    ) {
        val scrollState = rememberScrollState()
        Content(
            scrollState = scrollState,
            title = title,
            content = content,
            imageId = imageId,
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
    title: String,
    content: @Composable (ColumnScope.() -> Unit),
    imageId: Int,
    footer: @Composable (() -> Unit)?,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Main(
            title = title,
            content = content,
            imageId = imageId,
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState),
        )
        if (footer != null) {
            Box(
                modifier = Modifier,
            ) {
                footer()
            }
        }
    }

}

@Composable
private fun Main(
    title: String,
    content: @Composable (ColumnScope.() -> Unit),
    imageId: Int,
    modifier: Modifier = Modifier,
) {
    val horizontal = 16.dp + dimensionResource(R.dimen.margin_16_32_64)
    Column(
        modifier = modifier
            .padding(
                horizontal = horizontal,
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
        ) {
            Spacer(
                modifier = Modifier
                    .height(40.dp),
            )
            Text(
                text = title,
                color = LocalAppColors.current.blackOrWhite,
                style = LocalAppTypography.current.header,
                modifier = Modifier,
            )
            Spacer(
                modifier = Modifier
                    .height(12.dp),
            )
            content()
        }
        Spacer(
            modifier = Modifier
                .height(24.dp),
        )
        AppExplanationImage(imageId = imageId)
    }
}

@PreviewLightDark
@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun AppContentToReadPreview(
    @PreviewParameter(
        provider = AppContentToReadMocks::class,
    )
    text: String,
) {
    GroceryListTheme {
        AppContentToRead(
            modifier = Modifier
                .background(LocalAppColors.current.background),
            content = {
                Text(
                    text = text,
                    color = LocalAppColors.current.blackOrWhite,
                )
            },
            title = "Title",
            imageId = R.drawable.ic_android_200,
            footer = {
                Text(
                    text = "Footer",
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(LocalAppColors.current.brand_60_50)
                        .padding(24.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            },
        )
    }
}

private class AppContentToReadMocks : PreviewParameterProvider<String> {

    override val values = sequenceOf(
        "Short Text",
        LoremIpsumShortFragment,
        LoremIpsum,
    )
}
const val LoremIpsumShortFragment = "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
const val LoremIpsum = """
Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed dictum condimentum nibh, in dignissim quam pellentesque in. Phasellus eget vehicula velit. Fusce suscipit scelerisque pretium. Ut at enim augue. Quisque ut suscipit quam. Mauris et massa id magna sollicitudin laoreet bibendum non turpis. Interdum et malesuada fames ac ante ipsum primis in faucibus. Nunc faucibus accumsan justo sed facilisis. Etiam pharetra ipsum et laoreet posuere. Quisque efficitur diam sed leo molestie consectetur. Mauris vel odio venenatis, sollicitudin ligula at, consectetur magna. Nam id iaculis nunc.

Curabitur eget quam nec turpis hendrerit semper. Suspendisse sit amet dui purus. Sed pellentesque vitae nibh id convallis. Pellentesque congue, mauris at elementum commodo, nunc ex scelerisque magna, non suscipit risus metus vitae sem. Proin ac velit elit. Interdum et malesuada fames ac ante ipsum primis in faucibus. Integer posuere fringilla velit eu tincidunt. Donec ornare quam ut sapien vulputate, sed gravida diam convallis. Maecenas vel malesuada sapien, in molestie erat. Phasellus et lectus dictum arcu condimentum viverra a eu urna. Pellentesque sagittis sapien hendrerit lorem consequat, eget laoreet lorem sodales. Donec pharetra at est et ullamcorper. Aliquam vitae finibus neque. Duis porttitor at felis nec vulputate. Aliquam molestie, diam ut faucibus aliquet, ligula ligula tempus libero, ac tincidunt dolor erat a urna.

Sed cursus orci nec magna blandit condimentum. Etiam fringilla sapien at aliquam placerat. Donec maximus tincidunt consequat. In hac habitasse platea dictumst. Integer suscipit vehicula lacus, eget aliquam enim condimentum in. Donec pretium egestas ligula ut fringilla. Nullam vestibulum iaculis risus lacinia malesuada. Maecenas pellentesque est metus, sit amet tempus est fringilla id. Nulla finibus malesuada lectus sed scelerisque.

Curabitur ultrices elementum facilisis. Sed a viverra purus. Donec sed lectus in massa aliquet commodo sit amet eget dolor. Pellentesque et dictum sem. Quisque id turpis nisl. Nulla commodo erat ac blandit tempor. Nam euismod, sapien vitae egestas convallis, nunc libero laoreet leo, auctor porttitor est lorem quis ligula. Morbi in vehicula turpis.

Donec ligula arcu, feugiat in odio ac, venenatis lobortis diam. Aenean eu lectus pharetra, semper leo ut, venenatis nisl. Curabitur hendrerit turpis id est porta euismod. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Pellentesque congue fermentum libero vel varius. Sed id interdum risus. Maecenas porta turpis ut odio porta, a mattis nibh condimentum. Proin ornare sed metus sed consectetur. Nulla posuere est a lectus mattis consequat ut ut ipsum. Praesent dignissim arcu sit amet massa sollicitudin laoreet nec eu mauris. Suspendisse potenti. Nulla ullamcorper elementum sapien, vitae elementum nunc vulputate vel. Proin sed augue eu tortor malesuada posuere sed a ligula.
"""
