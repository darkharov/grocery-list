package app.grocery.list.product.input.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import app.grocery.list.commons.compose.AppTextField
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.elements.app.button.AppButton
import app.grocery.list.commons.compose.elements.app.button.AppButtonProps
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.values.StringValue
import app.grocery.list.product.input.form.screen.elements.category.picker.CategoryPicker
import app.grocery.list.product.input.form.screen.elements.category.picker.CategoryProps
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
    val categoryFocusRequester = remember { FocusRequester() }
    val selectedCategory = props.selectedCategory
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    val titleFocusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        titleFocusRequester.requestFocus()
    }
    Spacer(
        modifier = Modifier
            .height(dimensionResource(R.dimen.product_input_form_top_offset))
    )
    AppTextField(
        value = props.title,
        onValueChange = { newValue ->
            callbacks.onProductTitleChange(newValue)
        },
        modifier = Modifier
            .padding(
                horizontal = horizontalOffset,
            )
            .focusRequester(titleFocusRequester)
            .fillMaxWidth(),
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
                selectedCategory = selectedCategory,
                categoryFocusRequester = categoryFocusRequester,
                titleFocusRequester = titleFocusRequester,
                callbacks = callbacks,
                softwareKeyboardController = softwareKeyboardController,
            )
        },
        singleLine = true,
    )
    CategoryPicker(
        categories = props.categories,
        selection = selectedCategory,
        callbacks = callbacks,
        focusRequester = categoryFocusRequester,
        onSelectionComplete = {
            titleFocusRequester.requestFocus()
        },
        modifier = Modifier
            .padding(horizontal = horizontalOffset),
    )
    Spacer(
        modifier = Modifier
            .height(32.dp),
    )
    Buttons(
        horizontalOffset = horizontalOffset,
        props = props,
        selectedCategory = selectedCategory,
        categoryFocusRequester = categoryFocusRequester,
        titleFocusRequester = titleFocusRequester,
        callbacks = callbacks,
        softwareKeyboardController = softwareKeyboardController,
    )
}

private fun finalizeInput(
    props: ProductInputFormProps,
    selectedCategory: CategoryProps?,
    categoryFocusRequester: FocusRequester,
    titleFocusRequester: FocusRequester,
    callbacks: ProductInputFormCallbacks,
    softwareKeyboardController: SoftwareKeyboardController?,
) {
    if (props.title.isNotBlank()) {
        if (selectedCategory != null) {
            titleFocusRequester.requestFocus()
            callbacks.onProductInputComplete(
                productTitle = props.title,
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
    selectedCategory: CategoryProps?,
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
                enabled = props.title.isNotBlank(),
            ),
            onClick = {
                finalizeInput(
                    props = props,
                    selectedCategory = selectedCategory,
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
                enabled = props.atLeastOneProductAdded && props.title.isBlank(),
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
                        title = "",
                        categories = ProductInputFormMocks.categories.toImmutableList(),
                        selectedCategory = null,
                        atLeastOneProductAdded = true,
                    )
                )
            }
            ProductInputFormScreen(
                props = props,
                callbacks = object : ProductInputFormCallbacksMock() {
                    override fun onProductTitleChange(newValue: String) {}
                    override fun onProductInputComplete(productTitle: String, categoryId: Int) {}
                },
                modifier = Modifier
                    .padding(padding),
            )
        }
    }
}
