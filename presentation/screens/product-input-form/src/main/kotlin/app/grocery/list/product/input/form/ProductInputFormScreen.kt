package app.grocery.list.product.input.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.elements.AppTextField
import app.grocery.list.commons.compose.elements.button.AppButton
import app.grocery.list.commons.compose.elements.button.AppButtonProps
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.product.input.form.elements.category.picker.CategoryPicker
import app.grocery.list.product.input.form.elements.category.picker.CategoryPickerProps
import app.grocery.list.product.input.form.elements.category.picker.CategoryProps
import kotlinx.serialization.Serializable

@Serializable
data class ProductInputForm(
    val productId: Int?,
)

fun NavGraphBuilder.productInputFormScreen(
    navigation: ProductInputFormNavigation,
) {
    composable<ProductInputForm> { navBackStackEntry ->
        ProductInputFormScreen(
            arguments = navBackStackEntry.toRoute<ProductInputForm>(),
            navigation = navigation,
        )
    }
}

@Composable
internal fun ProductInputFormScreen(
    arguments: ProductInputForm,
    navigation: ProductInputFormNavigation,
) {
    val productId = arguments.productId
    val viewModel = hiltViewModel<ProductInputFormViewModel, ProductInputFormViewModel.Factory>(
        creationCallback = { factory ->
            factory.create(productId = productId)
        }
    )
    val title by viewModel.title().collectAsStateWithLifecycle()
    val emoji by viewModel.emoji.collectAsStateWithLifecycle()
    val categoryPicker by viewModel.categoryPicker.collectAsStateWithLifecycle()
    val atLeastOneProductJustAdded by viewModel.atLeastOneProductJustAdded.collectAsStateWithLifecycle()
    EventConsumer(viewModel.events()) { event ->
        when (event) {
            ProductInputFormViewModel.Event.OnDone -> {
                navigation.exitFromProductInputForm()
            }
        }
    }
    ProductInputFormScreen(
        title = title,
        emoji = emoji,
        categoryPicker = categoryPicker,
        atLeastOneProductJustAdded = atLeastOneProductJustAdded,
        editingMode = (productId != null),
        callbacks = viewModel,
    )
}

@Composable
internal fun ProductInputFormScreen(
    title: TextFieldValue,
    emoji: EmojiProps?,
    categoryPicker: CategoryPickerProps,
    atLeastOneProductJustAdded: Boolean,
    editingMode: Boolean,
    callbacks: ProductInputFormCallbacks,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .windowInsetsPadding(
                WindowInsets
                    .systemBars
                    .only(WindowInsetsSides.Bottom)
            ),
    ) {
        val horizontalOffset = dimensionResource(R.dimen.margin_16_32_64)
        val titleFocusRequester = remember { FocusRequester() }
        val categoryFocusRequester = remember { FocusRequester() }
        val selectedCategory = categoryPicker.selectedCategory
        val softwareKeyboardController = LocalSoftwareKeyboardController.current
        LaunchedEffect(Unit) {
            titleFocusRequester.requestFocus()
        }
        val topOffset = dimensionResource(R.dimen.margin_80_land_0)
        Spacer(
            modifier = Modifier
                .height(topOffset)
        )
        TitleAndEmoji(
            title = title,
            emoji = emoji,
            selectedCategory = selectedCategory,
            atLeastOneProductJustAdded = atLeastOneProductJustAdded,
            callbacks = callbacks,
            categoryFocusRequester = categoryFocusRequester,
            titleFocusRequester = titleFocusRequester,
            horizontalOffset = horizontalOffset,
            softwareKeyboardController = softwareKeyboardController,
            editingMode = editingMode,
            modifier = Modifier,
        )
        CategoryPicker(
            props = categoryPicker,
            callbacks = callbacks,
            focusRequester = categoryFocusRequester,
            modifier = Modifier
                .padding(
                    horizontal = horizontalOffset,
                ),
        )
        Spacer(
            modifier = Modifier
                .height(32.dp),
        )
        Buttons(
            horizontalOffset = horizontalOffset,
            editingMode = editingMode,
            title = title.text,
            emoji = emoji,
            selectedCategory = selectedCategory,
            atLeastOneProductJustAdded = atLeastOneProductJustAdded,
            callbacks = callbacks,
            categoryFocusRequester = categoryFocusRequester,
            titleFocusRequester = titleFocusRequester,
            softwareKeyboardController = softwareKeyboardController,
            modifier = Modifier,
        )
    }
}

@Composable
private fun TitleAndEmoji(
    title: TextFieldValue,
    emoji: EmojiProps?,
    selectedCategory: CategoryProps?,
    atLeastOneProductJustAdded: Boolean,
    callbacks: ProductInputFormCallbacks,
    horizontalOffset: Dp,
    titleFocusRequester: FocusRequester,
    categoryFocusRequester: FocusRequester,
    softwareKeyboardController: SoftwareKeyboardController?,
    editingMode: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(
                horizontal = horizontalOffset,
            ),
        verticalAlignment = Alignment.Bottom,
    ) {
        AppTextField(
            value = title,
            onValueChange = { newValue ->
                callbacks.onProductTitleChange(newValue)
            },
            modifier = Modifier
                .weight(5f)
                .focusRequester(titleFocusRequester),
            label = StringValue.ResId(R.string.product_to_buy_label),
            placeholder = StringValue.ResId(R.string.broccoli),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = if (selectedCategory == null) {
                    ImeAction.Next
                } else {
                    ImeAction.Done
                }
            ),
            keyboardActions = KeyboardActions {
                finalizeInput(
                    title = title.text,
                    selectedCategory = selectedCategory,
                    emoji = emoji,
                    atLeastOneProductAdded = atLeastOneProductJustAdded,
                    categoryFocusRequester = categoryFocusRequester,
                    titleFocusRequester = titleFocusRequester,
                    editingMode = editingMode,
                    callbacks = callbacks,
                    softwareKeyboardController = softwareKeyboardController,
                )
            },
            singleLine = true,
        )
        Spacer(
            modifier = Modifier
                .width(16.dp),
        )
        AppTextField(
            value = emoji?.value.orEmpty(),
            onValueChange = {},
            modifier = Modifier
                .weight(2f),
            label = StringValue.ResId(R.string.emoji),
            readOnly = true,
            singleLine = true,
        )
    }
}

private fun finalizeInput(
    title: String,
    selectedCategory: CategoryProps?,
    emoji: EmojiProps?,
    atLeastOneProductAdded: Boolean,
    categoryFocusRequester: FocusRequester,
    titleFocusRequester: FocusRequester,
    editingMode: Boolean,
    callbacks: ProductInputFormCallbacks,
    softwareKeyboardController: SoftwareKeyboardController?,
) {
    if (title.isNotBlank()) {
        if (selectedCategory != null) {
            titleFocusRequester.requestFocus()
            callbacks.onProductInputComplete(
                productTitle = title,
                categoryId = selectedCategory.id,
                emoji = emoji,
            )
            if (editingMode) {
                callbacks.onComplete()
            }
        } else {
            categoryFocusRequester.requestFocus()
            callbacks.onCategoryPickerExpandChange(true)
        }
    } else if (atLeastOneProductAdded) {
        softwareKeyboardController?.hide()
        callbacks.onComplete()
    }
}

@Composable
private fun Buttons(
    horizontalOffset: Dp,
    editingMode: Boolean,
    title: String,
    emoji: EmojiProps?,
    selectedCategory: CategoryProps?,
    atLeastOneProductJustAdded: Boolean,
    callbacks: ProductInputFormCallbacks,
    categoryFocusRequester: FocusRequester,
    titleFocusRequester: FocusRequester,
    softwareKeyboardController: SoftwareKeyboardController?,
    modifier: Modifier = Modifier,
) {
    if (editingMode) {
        AppButton(
            props = AppButtonProps.Done(),
            onClick = {
                finalizeInput(
                    title = title,
                    categoryFocusRequester = categoryFocusRequester,
                    selectedCategory = selectedCategory,
                    emoji = emoji,
                    atLeastOneProductAdded = atLeastOneProductJustAdded,
                    titleFocusRequester = titleFocusRequester,
                    callbacks = callbacks,
                    editingMode = editingMode,
                    softwareKeyboardController = softwareKeyboardController,
                )
            },
            modifier = Modifier
                .padding(
                    horizontal = horizontalOffset,
                ),
        )
    } else {
        Row(
            modifier = modifier
                .padding(horizontal = horizontalOffset),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AppButton(
                props = AppButtonProps.Custom(
                    text = stringResource(R.string.add),
                    state = AppButtonProps.State.enabled(title.isNotBlank()),
                ),
                onClick = {
                    finalizeInput(
                        title = title,
                        categoryFocusRequester = categoryFocusRequester,
                        selectedCategory = selectedCategory,
                        emoji = emoji,
                        atLeastOneProductAdded = atLeastOneProductJustAdded,
                        titleFocusRequester = titleFocusRequester,
                        editingMode = editingMode,
                        callbacks = callbacks,
                        softwareKeyboardController = softwareKeyboardController,
                    )
                },
                modifier = Modifier
                    .weight(1f),
            )
            AppButton(
                props = AppButtonProps.Done(
                    state = AppButtonProps.State.enabled(
                        enabled = atLeastOneProductJustAdded && title.isBlank(),
                    ),
                ),
                onClick = {
                    softwareKeyboardController?.hide()
                    callbacks.onComplete()
                },
                modifier = Modifier
                    .weight(1f),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun ProductInputScreenInitialStatePreview() {
    GroceryListTheme {
        Scaffold { padding ->
            ProductInputFormScreen(
                title = TextFieldValue(""),
                emoji = EmojiProps(
                    value = "🍎",
                ),
                categoryPicker = CategoryPickerProps(),
                atLeastOneProductJustAdded = false,
                editingMode = false,
                callbacks = ProductInputFormCallbacksMock,
                modifier = Modifier
                    .padding(padding),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun ProductInputScreenPreview() {
    GroceryListTheme {
        Scaffold { padding ->
            ProductInputFormScreen(
                title = TextFieldValue(""),
                emoji = EmojiProps(
                    value = "🍎",
                ),
                categoryPicker = CategoryPickerProps(),
                atLeastOneProductJustAdded = false,
                editingMode = false,
                callbacks = ProductInputFormCallbacksMock,
                modifier = Modifier
                    .padding(padding),
            )
        }
    }
}
