package app.grocery.list.preparing.for_.shopping

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.grocery.list.commons.compose.elements.AppContentToRead
import app.grocery.list.commons.compose.theme.GroceryListTheme
import kotlinx.serialization.Serializable

@Serializable
data object PreparingForShopping

fun NavGraphBuilder.preparingForShopping() {
    composable<PreparingForShopping> {
        PreparingForShopping()
    }
}

@Composable
private fun PreparingForShopping(
    modifier: Modifier = Modifier,
) {
    AppContentToRead(
        modifier = modifier,
        title = stringResource(R.string.final_steps),
        content = {
            StepsToTake()
        },
        imageId = R.drawable.lock_the_screen,
    )
}

@Composable
private fun StepsToTake() {
    val items = stringArrayResource(R.array.preparing_for_shopping_steps)
    for (item in items) {
        Row {
            Text(
                text = "\u2022 ",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
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
private fun PreparingForShoppingPreview() {
    GroceryListTheme {
        Scaffold { padding ->
            PreparingForShopping(
                modifier = Modifier
                    .padding(padding),
            )
        }
    }
}
