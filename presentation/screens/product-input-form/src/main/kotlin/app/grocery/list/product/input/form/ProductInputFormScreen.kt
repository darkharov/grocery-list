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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.KeyboardOnComposition
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
    val titleFocusRequester = remember { FocusRequester() }
    val categoryFocusRequester = remember { FocusRequester() }
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    KeyboardOnComposition(
        focusRequester = titleFocusRequester,
    )
    EventConsumer(
        viewModel = viewModel,
        navigation = navigation,
        titleFocusRequester = titleFocusRequester,
        softwareKeyboardController = softwareKeyboardController,
        categoryFocusRequester = categoryFocusRequester
    )
    ProductInputFormScreen(
        title = title,
        emoji = emoji,
        categoryPicker = categoryPicker,
        atLeastOneProductJustAdded = atLeastOneProductJustAdded,
        editingMode = (productId != null),
        titleFocusRequester = titleFocusRequester,
        categoryFocusRequester = categoryFocusRequester,
        callbacks = viewModel,
    )
}

@Composable
private fun EventConsumer(
    viewModel: ProductInputFormViewModel,
    navigation: ProductInputFormNavigation,
    titleFocusRequester: FocusRequester,
    softwareKeyboardController: SoftwareKeyboardController?,
    categoryFocusRequester: FocusRequester,
) {
    EventConsumer(viewModel.events()) { event ->
        when (event) {
            ProductInputFormViewModel.Event.ProductAdded -> {
                titleFocusRequester.requestFocus()
                softwareKeyboardController?.show()
            }
            ProductInputFormViewModel.Event.Completed -> {
                navigation.exitFromProductInputForm()
            }
            ProductInputFormViewModel.Event.TitleNotSpecified -> {
                titleFocusRequester.requestFocus()
                softwareKeyboardController?.show()
            }
            ProductInputFormViewModel.Event.CategoryNotSpecified -> {
                categoryFocusRequester.requestFocus()
            }
            ProductInputFormViewModel.Event.CategoryExplicitlySelected -> {
                titleFocusRequester.requestFocus()
                softwareKeyboardController?.show()
            }
        }
    }
}

@Composable
internal fun ProductInputFormScreen(
    title: TextFieldValue,
    emoji: EmojiProps?,
    categoryPicker: CategoryPickerProps,
    atLeastOneProductJustAdded: Boolean,
    editingMode: Boolean,
    callbacks: ProductInputFormCallbacks,
    categoryFocusRequester: FocusRequester,
    titleFocusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    val horizontalOffset = dimensionResource(R.dimen.margin_16_32_64)
    Column(
        modifier = modifier
            .windowInsetsPadding(
                WindowInsets
                    .systemBars
                    .only(WindowInsetsSides.Bottom)
            )
            .padding(
                horizontal = horizontalOffset,
            ),
    ) {
        val selectedCategory = categoryPicker.selectedCategory
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
            titleFocusRequester = titleFocusRequester,
        )
        CategoryPicker(
            props = categoryPicker,
            callbacks = callbacks,
            focusRequester = categoryFocusRequester,
        )
        Spacer(
            modifier = Modifier
                .height(32.dp),
        )
        Buttons(
            editingMode = editingMode,
            title = title.text,
            emoji = emoji,
            selectedCategory = selectedCategory,
            atLeastOneProductJustAdded = atLeastOneProductJustAdded,
            callbacks = callbacks,
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
    titleFocusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
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
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions {
                callbacks.onAttemptToCompleteProductInput(
                    productTitle = title.text,
                    selectedCategoryId = selectedCategory?.id,
                    emoji = emoji,
                    atLeastOneProductJustAdded = atLeastOneProductJustAdded,
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

@Composable
private fun Buttons(
    editingMode: Boolean,
    title: String,
    emoji: EmojiProps?,
    selectedCategory: CategoryProps?,
    atLeastOneProductJustAdded: Boolean,
    callbacks: ProductInputFormCallbacks,
    modifier: Modifier = Modifier,
) {
    if (editingMode) {
        AppButton(
            props = AppButtonProps.Done(),
            onClick = {
                callbacks.onAttemptToCompleteProductInput(
                    productTitle = title,
                    selectedCategoryId = selectedCategory?.id,
                    emoji = emoji,
                    atLeastOneProductJustAdded = atLeastOneProductJustAdded,
                )
            },
            modifier = modifier,
        )
    } else {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AppButton(
                props = AppButtonProps.Custom(
                    text = stringResource(R.string.add),
                    state = AppButtonProps.State.enabled(title.isNotBlank()),
                ),
                onClick = {
                    callbacks.onAttemptToCompleteProductInput(
                        productTitle = title,
                        selectedCategoryId = selectedCategory?.id,
                        emoji = emoji,
                        atLeastOneProductJustAdded = atLeastOneProductJustAdded,
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
                titleFocusRequester = remember { FocusRequester() },
                categoryFocusRequester = remember { FocusRequester() },
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
                titleFocusRequester = remember { FocusRequester() },
                categoryFocusRequester = remember { FocusRequester() },
                modifier = Modifier
                    .padding(padding),
            )
        }
    }
}
