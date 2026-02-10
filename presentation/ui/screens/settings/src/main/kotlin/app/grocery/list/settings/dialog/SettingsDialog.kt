package app.grocery.list.settings.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import app.grocery.list.commons.compose.elements.dialog.AppSimpleDialog
import app.grocery.list.commons.compose.theme.AppIcons
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.settings.R

@Composable
internal fun SettingsDialog(
    props: SettingsDialogProps,
    callbacks: SettingsDialogCallbacks,
) {
    when (props) {
        is SettingsDialogProps.BrowserNotFound -> {
            AppSimpleDialog(
                icon = rememberVectorPainter(AppIcons.browserNotSupported),
                text = StringValue.ResId(R.string.browser_app_not_found),
                onConfirm = {
                    callbacks.onDismiss()
                },
                onDismiss = {
                    callbacks.onDismiss()
                },
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun SettingsDialogPreview(
    @PreviewParameter(
        provider = SettingsDialogMocks::class,
    )
    props: SettingsDialogProps,
) {
    GroceryListTheme {
        SettingsDialog(
            props = props,
            callbacks = SettingsDialogCallbacksMock,
        )
    }
}
