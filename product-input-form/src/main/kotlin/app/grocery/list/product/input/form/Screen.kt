package app.grocery.list.product.input.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.grocery.list.commons.compose.elements.app.button.AppButton
import app.grocery.list.commons.compose.elements.app.button.AppButtonProps
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.product.input.form.screen.elements.category.picker.CategoryPicker
import app.grocery.list.product.input.form.screen.elements.category.picker.CategoryProps
import app.grocery.list.product.input.form.screen.elements.title.input.ProductTitleField
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
    LaunchedEffect(viewModel) {
        for (event in viewModel.events()) {
            when (event) {
                ProductInputFormViewModel.Event.OnGoToPreview -> {
                    navigation.onGoToPreview()
                }
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
) {
    if (props == null) {
        Preloader()
    } else {
        Form(props, callbacks)
    }
}

@Composable
private fun Preloader() {
    Box(
        modifier = Modifier
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
) {
    Column(
        modifier = Modifier
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
    ProductTitleField(
        title = props.title,
        callbacks = callbacks,
        focusRequester = titleFocusRequester,
        imeAction = if (selectedCategory == null) {
            ImeAction.Next
        } else {
            ImeAction.Done
        },
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
        modifier = Modifier
            .padding(horizontal = horizontalOffset),
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
    if (props.title.text.isNotBlank()) {
        if (selectedCategory != null) {
            titleFocusRequester.requestFocus()
            callbacks.onProductInputComplete(
                productTitle = props.title.text,
                categoryId = selectedCategory.id,
            )
        } else {
            categoryFocusRequester.requestFocus()
        }
    } else if (props.atLeastOneProductAdded) {
        softwareKeyboardController?.hide()
        callbacks.onReadyToGoToPreview()
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
                enabled = props.title.text.isNotBlank(),
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
            props = AppButtonProps.Next(
                enabled = props.atLeastOneProductAdded && props.title.text.isBlank(),
            ),
            onClick = {
                softwareKeyboardController?.hide()
                callbacks.onReadyToGoToPreview()
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
        ProductInputFormScreen(
            props = null,
            callbacks = ProductInputFormCallbacksMock(),
        )
    }
}

@PreviewLightDark
@Composable
private fun ProductInputScreenPreview() {
    GroceryListTheme {
        val props by remember {
            mutableStateOf(
                ProductInputFormProps(
                    title = TextFieldValue(),
                    categories = ProductInputFormMocks.categories.toImmutableList(),
                    selectedCategory = null,
                    atLeastOneProductAdded = false,
                )
            )
        }
        ProductInputFormScreen(
            props = props,
            callbacks = object : ProductInputFormCallbacksMock() {
                override fun onProductTitleChange(newValue: TextFieldValue) {}
                override fun onProductInputComplete(productTitle: String, categoryId: Int) {}
            }
        )
    }
}
