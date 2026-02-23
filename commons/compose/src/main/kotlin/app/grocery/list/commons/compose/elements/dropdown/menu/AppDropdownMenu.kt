package app.grocery.list.commons.compose.elements.dropdown.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import app.grocery.list.commons.compose.theme.LocalAppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDropdownMenu(
    props: AppDropdownMenuProps,
    onExpandedChange: (Boolean) -> Unit,
    onSelectionChange: (AppDropdownMenuProps.Item) -> Unit,
    focusRequester: FocusRequester = remember { FocusRequester() },
    modifier: Modifier = Modifier,
) {
    ExposedDropdownMenuBox(
        expanded = props.expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier,
    ) {
        AppTextField(
            value = props.selectedOne?.title.orEmpty(),
            onValueChange = {},
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged {
                    if (it.isFocused) {
                        onExpandedChange(true)
                    }
                },
            readOnly = true,
            label = props.label,
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
                onExpandedChange(false)
            },
            modifier = Modifier
                .background(LocalAppColors.current.background),
        ) {
            for (item in props.items) {
                AppDropdownMenuItem(
                    text = item.title,
                    onClick = {
                        onSelectionChange(item)
                    },
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun AppDropdownMenuPreview(
    @PreviewParameter(
        provider = AppDropdownMenuMocks ::class,
    )
    props: AppDropdownMenuProps,
) {
    GroceryListTheme {
        AppDropdownMenu(
            props = props,
            focusRequester = remember { FocusRequester() },
            onExpandedChange = {},
            onSelectionChange = {},
            modifier = Modifier,
        )
    }
}
