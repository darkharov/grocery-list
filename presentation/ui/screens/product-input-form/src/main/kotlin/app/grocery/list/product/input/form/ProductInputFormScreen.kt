package app.grocery.list.product.input.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.KeyboardOnComposition
import app.grocery.list.commons.compose.elements.AppAddAndDoneButtonPanel
import app.grocery.list.commons.compose.elements.AppTextField
import app.grocery.list.commons.compose.elements.button.AppButtonStateProps
import app.grocery.list.commons.compose.elements.dropdown.menu.AppDropdownMenu
import app.grocery.list.commons.compose.elements.dropdown.menu.AppDropdownMenuMocks
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors
import app.grocery.list.commons.compose.values.StringValue

@Composable
fun ProductInputFormScreen(
    productId: Int?,
    contract: ProductInputFormContract,
) {
    val productId = productId
    val viewModel = hiltViewModel<ProductInputFormViewModel, ProductInputFormViewModel.Factory>(
        creationCallback = { factory ->
            factory.create(productId = productId)
        }
    )
    val props by viewModel.props.collectAsStateWithLifecycle()
    val titleFocusRequester = remember { FocusRequester() }
    val categoryFocusRequester = remember { FocusRequester() }
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    KeyboardOnComposition(
        focusRequester = titleFocusRequester,
    )
    EventConsumer(
        viewModel = viewModel,
        contract = contract,
        titleFocusRequester = titleFocusRequester,
        softwareKeyboardController = softwareKeyboardController,
        categoryFocusRequester = categoryFocusRequester
    )
    Content(
        title = viewModel.title,
        props = props,
        titleFocusRequester = titleFocusRequester,
        categoryFocusRequester = categoryFocusRequester,
        callbacks = viewModel,
    )
}

@Composable
private fun EventConsumer(
    viewModel: ProductInputFormViewModel,
    contract: ProductInputFormContract,
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
                contract.goBack()
            }
            ProductInputFormViewModel.Event.TitleNotSpecified -> {
                titleFocusRequester.requestFocus()
                softwareKeyboardController?.show()
            }
            ProductInputFormViewModel.Event.CategoryNotSpecified -> {
                categoryFocusRequester.requestFocus()
            }
            ProductInputFormViewModel.Event.FocusOnTitle -> {
                titleFocusRequester.requestFocus()
                softwareKeyboardController?.show()
            }
        }
    }
}

@Composable
internal fun Content(
    title: TextFieldState,
    props: ProductInputFormProps,
    callbacks: ProductInputFormCallbacks,
    categoryFocusRequester: FocusRequester,
    titleFocusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    val horizontalOffset = dimensionResource(R.dimen.margin_16_32_64)
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = horizontalOffset,
            ),
    ) {
        val topOffset = dimensionResource(R.dimen.margin_80_land_0)
        Spacer(
            modifier = Modifier
                .height(topOffset)
        )
        TitleAndEmoji(
            title = title,
            props = props,
            callbacks = callbacks,
            titleFocusRequester = titleFocusRequester,
        )
        AppDropdownMenu(
            props = props.categoriesDropdown,
            onExpandedChange = { newValue ->
                callbacks.onCategoriesExpandedChange(newValue)
            },
            onSelectionChange = { item ->
                callbacks.onCategorySelected(item)
            },
            focusRequester = categoryFocusRequester,
        )
        AppDropdownMenu(
            props = props.productListsDropdown,
            onExpandedChange = { newValue ->
                callbacks.onProductListsExpandedChange(newValue)
            },
            onSelectionChange = { item ->
                callbacks.onProductListSelected(item)
            },
        )
        Spacer(
            modifier = Modifier
                .height(32.dp),
        )
        AppAddAndDoneButtonPanel(
            addButtonState = props.addButtonState,
            doneButtonState = props.doneButtonState,
            onAddClick = {
                callbacks.onAttemptToCompleteProductInput(
                    productTitle = title.text.toString(),
                    props = props,
                )
            },
            onDoneClick = {
                callbacks.onDone(
                    productTitle = title.text.toString(),
                    props = props,
                )
            },
        )
    }
}

@Composable
private fun TitleAndEmoji(
    title: TextFieldState,
    props: ProductInputFormProps,
    callbacks: ProductInputFormCallbacks,
    titleFocusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
    ) {
        AppTextField(
            state = title,
            modifier = Modifier
                .weight(5f)
                .focusRequester(titleFocusRequester),
            label = StringValue.ResId(R.string.product_title),
            placeholder = StringValue.ResId(R.string.broccoli),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done,
            ),
            onKeyboardAction = {
                callbacks.onAttemptToCompleteProductInput(
                    productTitle = title.text.toString(),
                    props = props,
                )
            },
            lineLimits = TextFieldLineLimits.SingleLine,
        )
        Spacer(
            modifier = Modifier
                .width(16.dp),
        )
        AppTextField(
            value = props.emoji
                ?.value
                ?: " ",    // Hack to keep label raised
            onValueChange = {},
            modifier = Modifier
                .weight(2f),
            label = StringValue.ResId(R.string.emoji),
            readOnly = true,
            singleLine = true,
        )
    }
}

@PreviewLightDark
@Composable
private fun ProductInputScreenPreview() {
    GroceryListTheme {
        Content(
            title = TextFieldState(),
            props = ProductInputFormProps(
                productId = 1,
                emoji = EmojiProps(
                    value = "🍎",
                ),
                categoriesDropdown = AppDropdownMenuMocks.prototype,
                enabled = true,
                addButtonState = AppButtonStateProps.Normal,
                doneButtonState = AppButtonStateProps.Disabled,
            ),
            callbacks = ProductInputFormCallbacksMock,
            titleFocusRequester = remember { FocusRequester() },
            categoryFocusRequester = remember { FocusRequester() },
            modifier = Modifier
                .background(LocalAppColors.current.background),
        )
    }
}
