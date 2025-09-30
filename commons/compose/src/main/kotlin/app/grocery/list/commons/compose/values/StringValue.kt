package app.grocery.list.commons.compose.values

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource

@Immutable
sealed class StringValue {

    @Composable
    @ReadOnlyComposable
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

        @Composable
        @ReadOnlyComposable
        override fun value(): String =
            value
    }

    @Immutable
    data class ResId(
        @StringRes
        val resId: Int,
        private val prefix: String = "",
        private val postfix: String = "",
        private val arguments: List<Any>? = null,
    ) : StringValue() {

        @Composable
        @ReadOnlyComposable
        override fun value(): String =
            "$prefix${unboxResource()}$postfix"

        @Composable
        @ReadOnlyComposable
        private fun unboxResource() =
            if (arguments == null) {
                stringResource(resId)
            } else {
                stringResource(resId, *arguments.toTypedArray())
            }
    }

    @Immutable
    data class PluralResId(
        @PluralsRes
        val resId: Int,
        val count: Int,
        val useCountAsArgument: Boolean = false,
    ) : StringValue() {

        @Composable
        @ReadOnlyComposable
        override fun value(): String =
            if (useCountAsArgument) {
                pluralStringResource(resId, count, count)
            } else {
                pluralStringResource(resId, count)
            }
    }
}
