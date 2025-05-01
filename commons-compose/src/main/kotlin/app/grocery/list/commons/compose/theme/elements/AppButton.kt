package app.grocery.list.commons.compose.theme.elements

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.NegativeActionColor
import app.grocery.list.commons.compose.theme.PositiveActionColor

enum class AppButtonType {
    Normal,
    Positive,
    Negative,
}

private val IconSize = 24.dp

@Composable
fun WideAppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes
    drawableEndId: Int? = null,
    type: AppButtonType = AppButtonType.Normal,
    enabled: Boolean = true,
) {
    AppButton(
        text = text,
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.margin_16_32_64)),
        enabled = enabled,
        type = type,
        drawableEndId = drawableEndId,
    )
}

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes
    drawableEndId: Int? = null,
    type: AppButtonType = AppButtonType.Normal,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = when (type) {
                AppButtonType.Normal -> MaterialTheme.colorScheme.primary
                AppButtonType.Positive -> PositiveActionColor
                AppButtonType.Negative -> NegativeActionColor
            },
        ),
    ) {
        if (drawableEndId != null) {
            Spacer(
                // just for symmetry
                modifier = Modifier
                    .size(IconSize),
            )
        }
        Text(
            text = text,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f),
        )
        if (drawableEndId != null) {
            Image(
                painter = painterResource(drawableEndId),
                contentDescription = null,
                modifier = Modifier
                    .width(IconSize),
                alignment = Alignment.CenterEnd,
            )
        }
    }
}

@Preview
@Composable
private fun AppButtonPreview() {
    GroceryListTheme {
        WideAppButton(
            text = "Text",
            onClick = {},
        )
    }
}
