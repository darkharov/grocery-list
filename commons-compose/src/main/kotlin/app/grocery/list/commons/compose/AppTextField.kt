package app.grocery.list.commons.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.values.StringValue

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    label: StringValue? = null,
    placeholder: StringValue? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        readOnly = readOnly,
        label = if (label == null) {
            null
        } else {
            {
                Text(
                    text = label.value(),
                )
            }
        },
        placeholder = if (placeholder == null) {
            null
        } else {
            {
                Text(
                    text = placeholder.value(),
                    modifier = Modifier
                        .alpha(0.2f),
                )
            }
        },
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        colors = TextFieldDefaults.colors(
            focusedLabelColor = if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.secondary
            },
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
        ),
    )
}

@PreviewLightDark
@Composable
private fun AppTextFieldPreview() {
    GroceryListTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            AppTextField(
                value = "",
                onValueChange = {},
                label = StringValue.StringWrapper("Label"),
                placeholder = StringValue.StringWrapper("Placeholder"),
                modifier = Modifier
                    .padding(12.dp),
            )
        }
    }
}