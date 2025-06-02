package app.grocery.list.commons.compose.elements.app.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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

private val IconSize = 24.dp

@Composable
fun WideAppButton(
    props: AppButtonProps,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.margin_16_32_64)),
        props = props,
    )
}

@Composable
fun AppButton(
    props: AppButtonProps,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = props.enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = props.background.toColor(),
        ),
    ) {
        if (props.drawableEndId != null) {
            Spacer(
                // just for symmetry
                modifier = Modifier
                    .size(IconSize),
            )
        }
        Text(
            text = props.title(),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f),
        )
        val drawableEndId = props.drawableEndId
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
            props = AppButtonProps.Custom(
                text = "Text",
            ),
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun AppButtonNextPreview() {
    GroceryListTheme {
        WideAppButton(
            props = AppButtonProps.Next(),
            onClick = {},
        )
    }
}
