package app.grocery.list.product.input.form.elements.category.picker

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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import app.grocery.list.commons.compose.elements.AppTextField
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.product.input.form.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CategoryPicker(
    props: CategoryPickerProps,
    focusRequester: FocusRequester,
    callbacks: CategoryPickerCallbacks,
    modifier: Modifier = Modifier,
) {
    ExposedDropdownMenuBox(
        expanded = props.expanded,
        onExpandedChange = { expanded ->
            callbacks.onCategoryPickerExpandChange(expanded = expanded)
        },
        modifier = modifier,
    ) {
        AppTextField(
            value = props.selectedCategory?.title.orEmpty(),
            onValueChange = {},
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged {
                    if (it.isFocused) {
                        callbacks.onCategoryPickerExpandChange(
                            expanded = true,
                        )
                    }
                },
            readOnly = true,
            label = StringValue.ResId(R.string.category),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = props.expanded,
                )
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next,
            ),
            singleLine = true,
        )
        ExposedDropdownMenu(
            expanded = props.expanded,
            onDismissRequest = {
                callbacks.onCategoryPickerExpandChange(
                    expanded = false,
                )
            },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
        ) {
            for (category in props.categories) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = category.title,
                        )
                    },
                    onClick = {
                        callbacks.onCategorySelected(
                            category = category,
                        )
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun CategoryPickerPreview(
    @PreviewParameter(
        provider = CategoryPickerMocks::class,
    )
    initial: CategoryPickerProps,
) {
    GroceryListTheme {
        var props by rememberSaveable { mutableStateOf(initial) }
        CategoryPicker(
            props = props,
            callbacks = object : CategoryPickerCallbacks {

                override fun onCategoryPickerExpandChange(expanded: Boolean) {
                    props = props.copy(expanded = expanded)
                }

                override fun onCategorySelected(category: CategoryProps) {
                    props = props.copy(selectedCategory = category)
                }
            },
            focusRequester = remember { FocusRequester() },
        )
    }
}
