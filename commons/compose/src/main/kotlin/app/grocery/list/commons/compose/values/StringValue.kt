package app.grocery.list.commons.compose.values

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource

@Immutable
sealed class StringValue {

    @Stable
    @Composable
    abstract fun value(): String

    @Deprecated(
        "It is probably a mistake. Use value() to convert this instance to String",
        ReplaceWith("this.value()")
    )
    override fun toString(): String {
        return super.toString()
    }

    @Immutable
    data class StringWrapper(
        private val value: String,
    ) : StringValue() {

        @Stable
        @Composable
        override fun value(): String =
            value
    }

    @Immutable
    data class ResId(
        @StringRes
        val resId: Int,
    ) : StringValue() {

        @Stable
        @Composable
        override fun value(): String =
            stringResource(resId)
    }

    @Immutable
    data class PluralResId(
        @PluralsRes
        val resId: Int,
        val count: Int,
        val useCountAsArgument: Boolean = false,
    ) : StringValue() {

        @Stable
        @Composable
        override fun value(): String =
            if (useCountAsArgument) {
                pluralStringResource(resId, count, count)
            } else {
                pluralStringResource(resId, count)
            }
    }
}
