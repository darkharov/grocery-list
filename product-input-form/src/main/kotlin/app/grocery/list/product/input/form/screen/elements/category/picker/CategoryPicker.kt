package app.grocery.list.product.input.form.screen.elements.category.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.product.input.form.R
import app.grocery.list.product.input.form.screen.ProductInputFormMocks
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CategoryPicker(
    selection: CategoryProps?,
    categories: ImmutableList<CategoryProps>,
    callbacks: CategoryPickerCallbacks,
    onSelectionComplete: () -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = it
        },
        modifier = modifier,
    ) {
        TextField(
            value = selection?.title.orEmpty(),
            onValueChange = {},
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged {
                    if (it.isFocused) {
                        expanded = true
                    }
                },
            readOnly = true,
            singleLine = true,
            label = {
                Text(
                    text = stringResource(R.string.category),
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded,
                )
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next,
            ),
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
            ),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
        ) {
            for (category in categories) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = category.title,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    },
                    onClick = {
                        expanded = false
                        callbacks.onCategorySelected(
                            category = category,
                        )
                        onSelectionComplete()
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun CategoryPickerPreview() {
    GroceryListTheme {
        CategoryPicker(
            selection = null,
            categories = ProductInputFormMocks.categories.toImmutableList(),
            callbacks = CategoryPickerCallbacksMock,
            onSelectionComplete = {},
            focusRequester = remember { FocusRequester() },
        )
    }
}
