package app.grocery.list.commons.compose.elements.app.button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppTypography

private val TrailingElementSize = 24.dp
private val TrailingElementOffset = 8.dp

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
            .padding(
                horizontal = dimensionResource(R.dimen.margin_16_32_64),
            ),
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
        shape = MaterialTheme.shapes.small,
        enabled = props.enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = props.background.toColor(),
        ),
    ) {
        Box(
            modifier = Modifier
                .weight(1f),
        ) {
            Text(
                text = props.title(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = if (props.hasTrailingElement) {
                            TrailingElementOffset + TrailingElementSize
                        } else {
                            0.dp
                        },
                    )
                    .align(Alignment.Center),
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = LocalAppTypography.current.button,
            )
            val drawableEndId = props.drawableEndId
            if (props.progressBar) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterEnd),
                )
            } else if (drawableEndId != null) {
                Icon(
                    painter = painterResource(drawableEndId),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = TrailingElementOffset)
                        .width(TrailingElementSize)
                        .align(Alignment.CenterEnd),
                )
            }
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
                drawableEndId = R.drawable.ic_android,
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

@Preview
@Composable
private fun AppButtonNextDisabledWithProgressBarPreview() {
    GroceryListTheme {
        WideAppButton(
            props = AppButtonProps.Custom(
                drawableEndId = R.drawable.ic_android,
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. ",
                state = AppButtonProps.State.DisabledWithProgressBar,
            ),
            onClick = {},
        )
    }
}
