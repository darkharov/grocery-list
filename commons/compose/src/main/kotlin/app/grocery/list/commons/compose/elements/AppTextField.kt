package app.grocery.list.commons.compose.elements

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppTypography
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.commons.compose.values.value

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
                AppPlaceholder(placeholder)
            }
        },
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        colors = appTextFieldColors(),
    )
}

@Composable
fun AppTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    label: StringValue? = null,
    placeholder: StringValue? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
) {
    TextField(
        state = state,
        modifier = modifier,
        readOnly = readOnly,
        label = if (label == null) {
            null
        } else {
            {
                Text(
                    text = label.value(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        placeholder = if (placeholder == null) {
            null
        } else {
            {
                AppPlaceholder(placeholder)
            }
        },
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        lineLimits = lineLimits,
        colors = appTextFieldColors(),
    )
}

@Composable
private fun AppPlaceholder(placeholder: StringValue) {
    Text(
        text = placeholder.value(),
        style = LocalAppTypography.current.plainText,
        modifier = Modifier
            .alpha(0.2f),
    )
}

@Composable
private fun appTextFieldColors() =
    TextFieldDefaults.colors(
        focusedLabelColor = if (isSystemInDarkTheme()) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.secondary
        },
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        errorContainerColor = Color.Transparent,
    )

@PreviewLightDark
@Composable
private fun AppTextFieldPreview() {
    GroceryListTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            AppTextField(
                value = "Text",
                onValueChange = {},
                label = StringValue.StringWrapper("Label"),
                placeholder = StringValue.StringWrapper("Placeholder"),
                modifier = Modifier
                    .padding(12.dp),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun EmptyAppTextFieldPreview() {
    GroceryListTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            var text by remember { mutableStateOf("") }
            AppTextField(
                value = text,
                onValueChange = {
                    text = it
                },
                label = StringValue.StringWrapper("Label"),
                placeholder = StringValue.StringWrapper("Placeholder"),
                modifier = Modifier
                    .padding(12.dp),
            )
        }
    }
}