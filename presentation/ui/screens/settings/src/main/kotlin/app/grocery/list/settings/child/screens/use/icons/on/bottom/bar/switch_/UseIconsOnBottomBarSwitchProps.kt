package app.grocery.list.settings.child.screens.use.icons.on.bottom.bar.switch_

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

@Immutable
data class UseIconsOnBottomBarSwitchProps(
    val checked: Boolean?,
    val visible: Boolean,
    val strategy: UseIconsOnBottomBarSwitchStrategy,
)

class UseIconsOnBottomBarSwitchMock : PreviewParameterProvider<UseIconsOnBottomBarSwitchProps> {

    override val values = UseIconsOnBottomBarSwitchStrategy
        .entries
        .mapIndexed { index, strategy ->
            UseIconsOnBottomBarSwitchProps(
                checked = index % 2 == 0,
                visible = true,
                strategy = strategy,
            )
        }
        .asSequence()
}
