package app.grocery.list.commons.compose.values

import android.content.res.Resources
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalResources

@Immutable
sealed class StringValue {

    abstract fun value(resources: Resources): String

    @Immutable
    data class StringWrapper(
        private val value: String,
    ) : StringValue() {

        override fun value(resources: Resources): String =
            value
    }

    @Immutable
    data class ResId(
        @param:StringRes
        val resId: Int,
        private val prefix: String = "",
        private val postfix: String = "",
        private val arguments: List<Any>? = null,
    ) : StringValue() {

        override fun value(resources: Resources): String =
            "$prefix${pureValue(resources)}$postfix"

        private fun pureValue(resources: Resources) =
            if (arguments == null) {
                resources.getString(resId)
            } else {
                resources.getString(resId, *arguments.toTypedArray())
            }
    }

    @Immutable
    data class PluralResId(
        @param:PluralsRes
        val resId: Int,
        val count: Int,
        val useCountAsArgument: Boolean = false,
    ) : StringValue() {

        override fun value(resources: Resources): String =
            if (useCountAsArgument) {
                resources.getQuantityString(resId, count, count)
            } else {
                resources.getQuantityString(resId, count)
            }
    }
}

@Composable
@ReadOnlyComposable
fun StringValue.value(): String =
    value(LocalResources.current)
