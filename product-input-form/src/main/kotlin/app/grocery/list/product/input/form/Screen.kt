package app.grocery.list.product.input.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.elements.AppTextField
import app.grocery.list.commons.compose.elements.app.button.AppButton
import app.grocery.list.commons.compose.elements.app.button.AppButtonProps
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.product.input.form.screen.elements.category.picker.CategoryPicker
import app.grocery.list.product.input.form.screen.elements.category.picker.CategoryPickerProps
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
        Preloader(modifier)
    } else {
        Form(props, callbacks, modifier)
    }
}

@Composable
private fun Preloader(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun Form(
    props: ProductInputFormProps,
    callbacks: ProductInputFormCallbacks,
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Elements(
            props = props,
            callbacks = callbacks,
        )
    }
}

@Composable
private fun Elements(
    props: ProductInputFormProps,
    callbacks: ProductInputFormCallbacks,
) {
    val horizontalOffset = dimensionResource(R.dimen.margin_16_32_64)
    val titleFocusRequester = remember { FocusRequester() }
    val categoryFocusRequester = remember { FocusRequester() }
    val selectedCategory = props.categoryPicker.selectedCategory
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(Unit) {
        titleFocusRequester.requestFocus()
    }
    Spacer(
        modifier = Modifier
            .height(dimensionResource(R.dimen.product_input_form_top_offset))
    )
    Row(
        modifier = Modifier
            .padding(
                horizontal = horizontalOffset,
            ),
        verticalAlignment = Alignment.Bottom,
    ) {
        AppTextField(
            value = props.title,
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
        props = props,
        categoryFocusRequester = categoryFocusRequester,
        titleFocusRequester = titleFocusRequester,
        callbacks = callbacks,
        softwareKeyboardController = softwareKeyboardController,
    )
}

private fun finalizeInput(
    props: ProductInputFormProps,
    categoryFocusRequester: FocusRequester,
    titleFocusRequester: FocusRequester,
    callbacks: ProductInputFormCallbacks,
    softwareKeyboardController: SoftwareKeyboardController?,
) {
    val selectedCategory = props.categoryPicker.selectedCategory
    if (props.title.isNotBlank()) {
        if (selectedCategory != null) {
            titleFocusRequester.requestFocus()
            callbacks.onProductInputComplete(
                productTitle = props.title,
                emoji = props.emoji,
                categoryId = selectedCategory.id,
            )
        } else {
            categoryFocusRequester.requestFocus()
        }
    } else if (props.atLeastOneProductAdded) {
        softwareKeyboardController?.hide()
        callbacks.onComplete()
    }
}

@Composable
private fun Buttons(
    horizontalOffset: Dp,
    props: ProductInputFormProps,
    categoryFocusRequester: FocusRequester,
    titleFocusRequester: FocusRequester,
    callbacks: ProductInputFormCallbacks,
    softwareKeyboardController: SoftwareKeyboardController?,
) {
    Row(
        modifier = Modifier
            .padding(horizontal = horizontalOffset),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        AppButton(
            props = AppButtonProps.Custom(
                text = stringResource(R.string.add),
                state = AppButtonProps.State.enabled(props.title.isNotBlank()),
            ),
            onClick = {
                finalizeInput(
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
                    enabled = props.atLeastOneProductAdded && props.title.isBlank(),
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
                callbacks = ProductInputFormCallbacksMock(),
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
                        title = "Lemon",
                        emoji = "\uD83C\uDF4B",
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
                callbacks = object : ProductInputFormCallbacksMock() {
                    override fun onProductTitleChange(newValue: String) {}
                    override fun onProductInputComplete(productTitle: String, emoji: String?, categoryId: Int) {}
                    override fun onCategoryPickerExpandChange(expanded: Boolean) {}
                },
                modifier = Modifier
                    .padding(padding),
            )
        }
    }
}
