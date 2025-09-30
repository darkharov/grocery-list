package app.grocery.list.commons.compose.elements.button

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppTypography
import app.grocery.list.commons.compose.values.StringValue

private val TrailingElementSize = 24.dp
private val TrailingElementOffset = 8.dp

@Composable
fun AppButton(
    title: StringValue,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    endIcon: Painter? = null,
    background: AppButtonBackgroundProps = AppButtonBackgroundProps.Normal,
    state: AppButtonStateProps = AppButtonStateProps.Enabled,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        enabled = state.enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = background.toColor(),
        ),
    ) {
        Box(
            modifier = Modifier
                .weight(1f),
        ) {
            Text(
                text = title.value(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = if (endIcon != null) {
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
            if (state.loading) {
                CircularProgressIndicator(
                    color = Color.Black,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterEnd),
                )
            } else if (endIcon != null) {
                Icon(
                    painter = endIcon,
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

@Composable
fun AppButtonDone(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    state: AppButtonStateProps = AppButtonStateProps.Enabled,
) {
    AppButton(
        title = StringValue.ResId(prefix = "✓ ", resId = R.string.done),
        modifier = modifier,
        endIcon = null,
        state = state,
        onClick = onClick,
    )
}

@Composable
fun AppButtonNext(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    state: AppButtonStateProps = AppButtonStateProps.Enabled,
    titleId: Int = R.string.next,
) {
    AppButton(
        title = StringValue.ResId(titleId, postfix = " >>"),
        modifier = modifier,
        endIcon = null,
        state = state,
        onClick = onClick,
    )
}

@Composable
fun AppButtonAdd(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    state: AppButtonStateProps = AppButtonStateProps.Enabled,
) {
    AppButton(
        title = StringValue.ResId(prefix = "+ ", resId = R.string.add),
        modifier = modifier,
        endIcon = null,
        background = AppButtonBackgroundProps.Normal,
        state = state,
        onClick = onClick,
    )
}

@Preview
@Composable
private fun AppButtonStatePreview(
    @PreviewParameter(
        provider = AppButtonStateMocks::class,
    )
    state: AppButtonStateProps
) {
    GroceryListTheme {
        AppButton(
            title = StringValue.StringWrapper("Title"),
            endIcon = painterResource(R.drawable.ic_android),
            onClick = {},
            state = state,
        )
    }
}

@Preview
@Composable
private fun AppButtonBackgroundPreview(
    @PreviewParameter(
        provider = AppButtonBackgroundMocks::class,
    )
    background: AppButtonBackgroundProps,
) {
    GroceryListTheme {
        AppButton(
            title = StringValue.StringWrapper("Title"),
            endIcon = painterResource(R.drawable.ic_android),
            onClick = {},
            background = background,
        )
    }
}
