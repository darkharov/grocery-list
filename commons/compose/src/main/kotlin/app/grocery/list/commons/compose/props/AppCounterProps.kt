package app.grocery.list.commons.compose.props

import androidx.compose.runtime.Immutable

@Immutable
sealed class AppCounterProps {

    abstract val formattedValue: String
    abstract val alpha: Float

    @Immutable
    data object NoItems : AppCounterProps() {
        override val alpha = 0.42f
        override val formattedValue = "(0)"
    }

    @Immutable
    class JustTotalSize(
        totalSize: Int,
    ) : AppCounterProps() {
        override val alpha = 1f
        override val formattedValue = "(${totalSize})"
    }

    @Immutable
    class Ratio(
        numberOfEnabled: Int,
        totalSize: Int,
    ) : AppCounterProps() {
        override val alpha = 1f
        override val formattedValue = "($numberOfEnabled/$totalSize)"
    }
}
