package app.grocery.list.final_.steps

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.grocery.list.commons.compose.elements.AppContentToRead
import app.grocery.list.commons.compose.theme.GroceryListTheme
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
        Row(
            modifier = Modifier
                .padding(bottom = 8.dp),
        ) {
            Text(
                text = "\u2022",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(
                modifier = Modifier
                    .width(8.dp),
            )
            Text(
                text = item,
                fontSize = 16.sp,
            )
        }
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
