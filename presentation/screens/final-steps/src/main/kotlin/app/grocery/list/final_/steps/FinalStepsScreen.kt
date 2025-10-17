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
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.grocery.list.commons.compose.elements.AppContentToRead
import app.grocery.list.commons.compose.elements.dialog.AppSimpleDialog
import app.grocery.list.commons.compose.theme.AppIcons
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppTypography
import app.grocery.list.commons.compose.values.StringValue
import kotlinx.serialization.Serializable

@Serializable
data object FinalSteps

fun NavGraphBuilder.finalSteps() {
    composable<FinalSteps> {
        FinalSteps()
    }
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
            isOffset = false,
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
            )
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(
                top = 6.dp,
                bottom = 12.dp,
            ),
    ) {
        BulletOrOffset(
            isOffset = true,
        )
        Text(
            text = text,
            style = LocalAppTypography.current.plainText,
            textDecoration = TextDecoration.Underline,
        )
        BulletOrOffset(
            isOffset = true,
        )
    }
}

@Composable
private fun BulletOrOffset(
    isOffset: Boolean,
) {
    Text(
        text = "•",
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        style = LocalAppTypography.current.plainText,
        modifier = Modifier
            .alpha(
                if (isOffset) {
                    0f
                } else {
                    1f
                },
            )
    )
    Spacer(
        modifier = Modifier
            .width(8.dp),
    )
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
