package app.grocery.list.commons.compose.elements.titled.switch_

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.grocery.list.commons.compose.elements.LoremIpsum
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppTypography
import app.grocery.list.commons.compose.theme.blackOrWhite
import app.grocery.list.commons.compose.values.StringValue

@Composable
fun AppSwitch(
    text: StringValue,
    checked: Boolean?,
    onCheckedChange: (newValue: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    description: StringValue? = null,
) {
    val shape = MaterialTheme.shapes.small
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        val strokeWidth = 2.dp
        Row(
            modifier = Modifier
                .clip(shape)
                .clickable {
                    if (checked != null) {
                        onCheckedChange(!(checked))
                    }
                }
                .border(
                    width = strokeWidth,
                    color = MaterialTheme.colorScheme.primary,
                    shape = shape,
                )
                .padding(vertical = 8.dp)
                .padding(start = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = text.value(),
                fontSize = 13.sp,
                color = MaterialTheme
                    .colorScheme
                    .blackOrWhite,
                modifier = Modifier
                    .weight(1f),
            )
            Spacer(
                modifier = Modifier
                    .padding(8.dp),
            )
            val height = 32.dp
            Box {
                if (checked == null) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(height)
                            .padding(4.dp),
                        strokeWidth = strokeWidth,
                    )
                } else {
                    Switch(
                        checked = checked,
                        onCheckedChange = null,
                        modifier = Modifier
                            .height(height),
                    )
                }
            }
        }
        if (description != null) {
            Text(
                text = description.value(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.blackOrWhite,
                textAlign = TextAlign.Center,
                style = LocalAppTypography.current.explanation,
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun AppSwitchPreview() {
    GroceryListTheme {
        var checked by remember { mutableStateOf(true) }
        AppSwitch(
            text = StringValue.StringWrapper("Title"),
            description = StringValue.StringWrapper(LoremIpsum.substring(0, 100)),
            checked = checked,
            onCheckedChange = { newValue ->
                checked = newValue
            },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
        )
    }
}

@Composable
@PreviewLightDark
private fun AppSwitchNullPreview() {
    GroceryListTheme {
        AppSwitch(
            text = StringValue.StringWrapper("Title"),
            description = StringValue.StringWrapper(LoremIpsum.substring(0, 100)),
            checked = null,
            onCheckedChange = {},
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
        )
    }
}
