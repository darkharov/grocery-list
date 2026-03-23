package app.grocery.list.custom.product.lists.input.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.KeyboardOnComposition
import app.grocery.list.commons.compose.elements.AppAddAndDoneButtonPanel
import app.grocery.list.commons.compose.elements.AppPreloaderOrContent
import app.grocery.list.commons.compose.elements.AppTextField
import app.grocery.list.commons.compose.elements.color.scheme.AppColorSchemePicker
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.custom.product.lists.R
import app.grocery.list.domain.product.list.ProductList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private typealias VM = CustomListInputFormViewModel
private typealias VMF = CustomListInputFormViewModel.Factory

@Composable
fun CustomListInputFormScreen(
    contract: CustomListInputFormContract,
    customListId: ProductList.Id.Custom?,
) {
    val viewModel = hiltViewModel<VM, VMF>(
        creationCallback = { factory ->
            factory.create(customListId)
        },
    )
    val props by viewModel.props.collectAsStateWithLifecycle()
    CustomListInputFormScreen(
        title = viewModel.title,
        props = props,
        callbacks = viewModel,
    )
    EventConsumer(viewModel.events()) { event ->
        when (event) {
            CustomListInputFormViewModel.Event.OnExit -> {
                contract.goBack()
            }
        }
    }
}

@Composable
internal fun CustomListInputFormScreen(
    title: TextFieldState,
    props: CustomListInputFormProps?,
    callbacks: CustomListInputFormCallbacks,
    modifier: Modifier = Modifier,
) {
    AppPreloaderOrContent(props) { props ->
        Content(
            title = title,
            props = props,
            callbacks = callbacks,
            modifier = modifier,
        )
    }
}

@Composable
private fun Content(
    title: TextFieldState,
    props: CustomListInputFormProps,
    callbacks: CustomListInputFormCallbacks,
    modifier: Modifier = Modifier,
) {
    val titleFocusRequester = remember { FocusRequester() }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val scope = rememberCoroutineScope()
    KeyboardOnComposition(
        focusRequester = titleFocusRequester,
    )
    Column(
        modifier = modifier
            .background(LocalAppColors.current.background)
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(
                vertical = 16.dp,
                horizontal = dimensionResource(R.dimen.margin_16_32_64),
            ),
    ) {
        Spacer(
            modifier = Modifier
                .weight(1f),
        )
        AppTextField(
            state = title,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(titleFocusRequester)
                .onFocusChanged { focusState ->
                    if (focusState.hasFocus) {
                        scope.launch {
                            delay(450)  // DO NOT DELETE!
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                },
            label = StringValue.ResId(R.string.product_list_title),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done,
            ),
            onKeyboardAction = {
                completeForm(
                    title = title,
                    props = props,
                    callbacks = callbacks,
                )
            },
            lineLimits = TextFieldLineLimits.SingleLine,
        )
        Spacer(
            modifier = Modifier
                .height(24.dp),
        )
        AppColorSchemePicker(
            selection = props.colorScheme,
            onSelectionChange = { newValue ->
                callbacks.onColorSchemeSelectionChange(newValue)
            },
            modifier = Modifier
                .padding(horizontal = 14.dp),
        )
        Spacer(
            modifier = Modifier
                .weight(1f),
        )
        AppAddAndDoneButtonPanel(
            addButtonState = props.addButtonState,
            doneButtonState = props.doneButtonState,
            onAddClick = {
                completeForm(
                    title = title,
                    props = props,
                    callbacks = callbacks,
                )
            },
            onDoneClick = {
                completeForm(
                    title = title,
                    props = props,
                    callbacks = callbacks,
                )
            },
            modifier = Modifier
                .bringIntoViewRequester(bringIntoViewRequester),
        )
    }
}

private fun completeForm(
    title: TextFieldState,
    props: CustomListInputFormProps,
    callbacks: CustomListInputFormCallbacks,
) {
    callbacks.onComplete(
        title = title.text.toString(),
        colorScheme = props.colorScheme,
    )
}

@Composable
@PreviewLightDark
private fun CustomListInputFormPreview(
    @PreviewParameter(
        provider = CustomListInputFormMock::class,
    )
    props: CustomListInputFormProps,
) {
    GroceryListTheme {
        CustomListInputFormScreen(
            title = rememberTextFieldState(),
            props = props,
            callbacks = CustomListInputFormCallbacksMock,
            modifier = Modifier,
        )
    }
}
