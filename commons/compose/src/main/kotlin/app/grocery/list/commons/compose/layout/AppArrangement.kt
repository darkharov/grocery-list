package app.grocery.list.commons.compose.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Density
import androidx.compose.ui.util.fastRoundToInt

@Immutable
object AppArrangement {

    @Stable
    val LastAtBottomRestAtTop = object : Arrangement.Vertical {

        override fun toString(): String =
            "AppArrangement#LastAtBottomRestAtTop"

        override fun Density.arrange(
            totalSize: Int,
            sizes: IntArray,
            outPositions: IntArray,
        ) {
            var current = 0
            sizes.forEachIndexed { index, it ->
                if (index < sizes.lastIndex) {
                    outPositions[index] = current
                    current += it
                } else {
                    outPositions[index] = totalSize - it
                }
            }
        }
    }

    @Stable
    val LastAtBottomRestInCenter = object : Arrangement.Vertical {

        override fun toString(): String =
            "AppArrangement#LastAtBottomRestInCenter"

        override fun Density.arrange(
            totalSize: Int,
            sizes: IntArray,
            outPositions: IntArray,
        ) {
            if (sizes.isEmpty()) {
                return
            }

            val sizeOfAllExpectLast = sizes
                .asSequence()
                .take(sizes.size - 1)
                .fold(0, Int::plus)

            var current = (totalSize - sizeOfAllExpectLast).toFloat() / 2

            sizes.forEachIndexed { index, it ->
                if (index < sizes.lastIndex) {
                    outPositions[index] = current.fastRoundToInt()
                    current += it
                } else {
                    outPositions[index] = totalSize - it
                }
            }
        }
    }
}
