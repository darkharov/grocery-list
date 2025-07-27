package app.grocery.list.product.input.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.elements.AppPreloader
import app.grocery.list.commons.compose.elements.AppTextField
import app.grocery.list.commons.compose.elements.button.AppButton
import app.grocery.list.commons.compose.elements.button.AppButtonProps
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.product.input.form.elements.category.picker.CategoryPicker
import app.grocery.list.product.input.form.elements.category.picker.CategoryPickerProps
import app.grocery.list.product.input.form.elements.category.picker.CategoryProps
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable

@Serializable
data object ProductInputForm

fun NavGraphBuilder.productInputFormScreen(
    navigation: ProductInputFormNavigation,
) {
    composable<ProductInputForm> {
        ProductInputFormScreen(
            navigation = navigation,
        )
    }
}

@Composable
internal fun ProductInputFormScreen(
    viewModel: ProductInputFormViewModel = hiltViewModel(),
    navigation: ProductInputFormNavigation,
) {
    val props by viewModel.props().collectAsState()
    EventConsumer(
        key = viewModel,
        lifecycleState = Lifecycle.State.RESUMED,
        events = viewModel.events(),
    ) { event ->
        when (event) {
            ProductInputFormViewModel.Event.OnDone -> {
                navigation.exitFromProductInputForm()
            }
        }
    }
    ProductInputFormScreen(
        props = props,
        callbacks = viewModel,
    )
}

@Composable
internal fun ProductInputFormScreen(
    props: ProductInputFormProps?,
    callbacks: ProductInputFormCallbacks,
    modifier: Modifier = Modifier,
) {
    if (props == null) {
        AppPreloader()
    } else {
        Form(props, callbacks, modifier)
    }
}

@Composable
private fun Form(
    props: ProductInputFormProps,
    callbacks: ProductInputFormCallbacks,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        val horizontalOffset = dimensionResource(R.dimen.margin_16_32_64)
        var title by rememberSaveable { mutableStateOf("") }
        val titleFocusRequester = remember { FocusRequester() }
        val categoryFocusRequester = remember { FocusRequester() }
        val selectedCategory = props.categoryPicker.selectedCategory
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
            horizontalOffset = horizontalOffset,
            title = title,
            onTitleChange = { newValue ->
                title = newValue
            },
            props = props,
            callbacks = callbacks,
            titleFocusRequester = titleFocusRequester,
            selectedCategory = selectedCategory,
            categoryFocusRequester = categoryFocusRequester,
            softwareKeyboardController = softwareKeyboardController,
            modifier = Modifier,
        )
        CategoryPicker(
            props = props.categoryPicker,
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
            title = title,
            onTitleChange = { newValue ->
                title = newValue
            },
            props = props,
            categoryFocusRequester = categoryFocusRequester,
            titleFocusRequester = titleFocusRequester,
            callbacks = callbacks,
            softwareKeyboardController = softwareKeyboardController,
            modifier = Modifier,
        )
    }
}

@Composable
private fun TitleAndEmoji(
    horizontalOffset: Dp,
    props: ProductInputFormProps,
    callbacks: ProductInputFormCallbacks,
    titleFocusRequester: FocusRequester,
    selectedCategory: CategoryProps?,
    categoryFocusRequester: FocusRequester,
    softwareKeyboardController: SoftwareKeyboardController?,
    modifier: Modifier = Modifier,
    title: String,
    onTitleChange: (String) -> Unit,
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
                onTitleChange(newValue)
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
                    title = title,
                    onTitleChange = onTitleChange,
                    props = props,
                    categoryFocusRequester = categoryFocusRequester,
                    titleFocusRequester = titleFocusRequester,
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
            value = props.emoji ?: " ",
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
    onTitleChange: (String) -> Unit,
    props: ProductInputFormProps,
    categoryFocusRequester: FocusRequester,
    titleFocusRequester: FocusRequester,
    callbacks: ProductInputFormCallbacks,
    softwareKeyboardController: SoftwareKeyboardController?,
) {
    val selectedCategory = props.categoryPicker.selectedCategory
    if (title.isNotBlank()) {
        if (selectedCategory != null) {
            titleFocusRequester.requestFocus()
            callbacks.onProductInputComplete(
                productTitle = title,
                categoryId = selectedCategory.id,
                payload = props.payload,
            )
            onTitleChange("")
            callbacks.onProductTitleChange("")
        } else {
            categoryFocusRequester.requestFocus()
            callbacks.onCategoryPickerExpandChange(true)
        }
    } else if (props.atLeastOneProductAdded) {
        softwareKeyboardController?.hide()
        callbacks.onComplete()
    }
}

@Composable
private fun Buttons(
    horizontalOffset: Dp,
    title: String,
    onTitleChange: (String) -> Unit,
    props: ProductInputFormProps,
    categoryFocusRequester: FocusRequester,
    titleFocusRequester: FocusRequester,
    callbacks: ProductInputFormCallbacks,
    softwareKeyboardController: SoftwareKeyboardController?,
    modifier: Modifier = Modifier,
) {
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
                    onTitleChange = onTitleChange,
                    props = props,
                    categoryFocusRequester = categoryFocusRequester,
                    titleFocusRequester = titleFocusRequester,
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
                    enabled = props.atLeastOneProductAdded && title.isBlank(),
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

@PreviewLightDark
@Composable
private fun ProductInputScreenInitialStatePreview() {
    GroceryListTheme {
        Scaffold { padding ->
            ProductInputFormScreen(
                props = null,
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
            val props by remember {
                mutableStateOf(
                    ProductInputFormProps(
                        emoji = "🍋",
                        categoryPicker = CategoryPickerProps(
                            categories = ProductInputFormMocks.categories.toImmutableList(),
                            selectedCategory = null,
                            expanded = false,
                        ),
                        atLeastOneProductAdded = true
                    )
                )
            }
            ProductInputFormScreen(
                props = props,
                callbacks = ProductInputFormCallbacksMock,
                modifier = Modifier
                    .padding(padding),
            )
        }
    }
}
