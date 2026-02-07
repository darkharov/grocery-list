package app.grocery.list.commons.compose.elements.button.text

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.elements.AppHorizontalDivider
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppTypography
import app.grocery.list.commons.compose.theme.blackOrWhite
import app.grocery.list.commons.compose.values.value

@Composable
fun AppTextButton(
    props: AppTextButtonProps,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(
                when (props) {
                    is AppTextButtonProps.SettingsCategory -> {
                        RectangleShape
                    }
                    is AppTextButtonProps.TextOnly -> {
                        RoundedCornerShape(8.dp)
                    }
                },
            )
            .then(
                if (props.enabled) {
                    Modifier.clickable {
                        onClick()
                    }
                } else {
                    Modifier
                        .alpha(0.33f)
                }
            ),
    ) {
        Row(
            modifier = Modifier
                .padding(props.padding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val leadingIconId = props.leadingIconId
            if (leadingIconId != null) {
                Icon(
                    painter = painterResource(leadingIconId),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                )
                Spacer(
                    modifier = Modifier
                        .padding(8.dp),
                )
            }
            Text(
                text = props.text.value(),
                color = props.textColor,
                textAlign = props.textAlign,
                style = LocalAppTypography.current.textButton,
            )
            val trailingIconId = props.trailingIconId
            if (trailingIconId != null) {
                Spacer(
                    modifier = Modifier
                        .weight(1f),
                )
                Icon(
                    painter = painterResource(R.drawable.ic_forward),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.blackOrWhite,
                )
            }
        }
        if (props.hasDivider) {
            AppHorizontalDivider()
        }
    }
}

@PreviewLightDark
@Composable
private fun AppTextButtonPreview(
    @PreviewParameter(
        provider = AppTextButtonMocks::class,
    )
    props: AppTextButtonProps,
) {
    GroceryListTheme {
        Scaffold { padding ->
            AppTextButton(
                props = props,
                onClick = {},
                modifier = Modifier
                    .padding(padding),
            )
        }
    }
}
