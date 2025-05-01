package app.grocery.list.product.input.form.screen.elements.title.input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.product.input.form.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductTitleField(
    title: TextFieldValue,
    callbacks: ProductTitleInputCallbacks,
    imeAction: ImeAction,
    focusRequester: FocusRequester,
    keyboardActions: KeyboardActions,
    modifier: Modifier = Modifier,
) {
    TextField(
        value = title,
        onValueChange = { newValue ->
            callbacks.onProductTitleChange(newValue)
        },
        modifier = modifier
            .focusRequester(focusRequester)
            .fillMaxWidth(),
        label = {
            Text(
                text = stringResource(R.string.product_to_buy_label),
            )
        },
        placeholder = {
            Text(
                text = stringResource(R.string.broccoli),
                modifier = Modifier
                    .alpha(0.2f),
            )
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = imeAction,
        ),
        keyboardActions = keyboardActions,
        singleLine = true,
        colors = ExposedDropdownMenuDefaults.textFieldColors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
        ),
    )
}

@PreviewLightDark
@Composable
private fun ProductTitleFieldPreview() {
    GroceryListTheme {
        ProductTitleField(
            title = TextFieldValue(),
            callbacks = ProductTitleInputCallbacksMock,
            focusRequester = remember { FocusRequester() },
            imeAction = ImeAction.Next,
            keyboardActions = KeyboardActions {},
        )
    }
}
