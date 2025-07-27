package app.grocery.list.commons.compose.elements

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.drawUpwardGradient
import app.grocery.list.commons.compose.theme.GroceryListTheme
import kotlin.math.min

@Composable
fun AppExplanationImage(
    @DrawableRes
    imageId: Int,
    modifier: Modifier = Modifier,
) {
    val width = with(LocalDensity.current) {
        with(LocalWindowInfo.current.containerSize) {
            min(width, height).toDp()
        }
    }
    Image(
        painter = painterResource(imageId),
        contentDescription = null,
        colorFilter = ColorFilter.tint(
            MaterialTheme.colorScheme.primary,
        ),
        modifier = modifier
            .width(width)
            .drawUpwardGradient(MaterialTheme.colorScheme.background),
    )
}

@PreviewLightDark
@Composable
private fun AppBottomExplanationImagePreview() {
    GroceryListTheme {
        Scaffold { padding ->
            AppExplanationImage(
                imageId = R.drawable.ic_android_200,
                modifier = Modifier
                    .padding(padding)
            )
        }
    }
}
