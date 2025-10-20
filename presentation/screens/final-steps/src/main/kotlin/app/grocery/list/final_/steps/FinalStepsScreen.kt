package app.grocery.list.final_.steps

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.elements.AppContentToRead
import app.grocery.list.commons.compose.elements.dialog.AppSimpleDialog
import app.grocery.list.commons.compose.theme.AppIcons
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppTypography
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.final_.steps.FinalStepsViewModel.Event
import kotlinx.serialization.Serializable

private val BulletOffset = 8.dp

@Serializable
data object FinalSteps

fun NavGraphBuilder.finalSteps(
    navigation: FinalStepsNavigation,
) {
    composable<FinalSteps> {
        FinalSteps(navigation = navigation)
    }
}

@Composable
private fun FinalSteps(
    navigation: FinalStepsNavigation,
) {
    val viewModel = hiltViewModel<FinalStepsViewModel>()
    EventConsumer(
        events = viewModel.events(),
    ) { event ->
        when (event) {
            is Event.OnNoEnabledProductsAnymore -> {
                navigation.backToActionsOrListPreview()
            }
        }
    }
    FinalSteps(
        modifier = Modifier,
    )
}

@Composable
private fun FinalSteps(
    modifier: Modifier = Modifier,
) {
    AppContentToRead(
        modifier = modifier,
        title = stringResource(R.string.final_steps),
        content = {
            Content()
        },
        imageId = R.drawable.lock_the_screen,
    )
}

@Composable
private fun Content() {
    val items = stringArrayResource(R.array.final_steps)
    for (item in items) {
        Item(text = item)
    }
    var fingerprintDialogShown by rememberSaveable { mutableStateOf(false) }
    ClickableLabel(
        text = stringResource(R.string.fingerprint_location_question),
        onClick = {
            fingerprintDialogShown = true
        },
    )
    if (fingerprintDialogShown) {
        AppSimpleDialog(
            icon = rememberVectorPainter(AppIcons.fingerprint),
            text = StringValue.ResId(R.string.fingerprint_location_answer),
            onDismiss = {
                fingerprintDialogShown = false
            },
            onConfirm = {
                fingerprintDialogShown = false
            }
        )
    }
}

@Composable
private fun Item(text: String) {
    Row(
        modifier = Modifier
            .padding(bottom = 8.dp),
    ) {
        BulletOrOffset(
            isBullet = true,
        )
        Text(
            text = text,
            style = LocalAppTypography.current.plainText,
        )
    }
}

@Composable
private fun ClickableLabel(
    text: String,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .padding(
                top = 16.dp,
            ),
    ) {
        BulletOrOffset(
            isBullet = false,
        )
        Text(
            text = text,
            style = LocalAppTypography.current.plainText,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { onClick() }
                .padding(horizontal = BulletOffset)
                .padding(
                    top = 6.dp,
                    bottom = 12.dp,
                ),
        )
        BulletOrOffset(
            isBullet = false,
        )
    }
}

@Composable
private fun BulletOrOffset(
    isBullet: Boolean,
) {
    Text(
        text = "•",
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        style = LocalAppTypography.current.plainText,
        modifier = Modifier
            .alpha(
                if (isBullet) {
                    1f
                } else {
                    0f
                },
            ),
    )
    if (isBullet) {
        Spacer(
            modifier = Modifier
                .width(BulletOffset),
        )
    }
}

@PreviewLightDark
@Preview(device = "spec:width=1280dp,height=800dp,dpi=240,orientation=portrait")
@Composable
private fun FinalStepsPreview() {
    GroceryListTheme {
        Scaffold { padding ->
            FinalSteps(
                modifier = Modifier
                    .padding(padding),
            )
        }
    }
}
