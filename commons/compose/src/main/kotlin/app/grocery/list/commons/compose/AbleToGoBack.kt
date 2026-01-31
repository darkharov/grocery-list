package app.grocery.list.commons.compose

import androidx.compose.runtime.Stable

@Stable
fun interface AbleToGoBack {
    fun goBack()
}

object AbleToGoBackMock : AbleToGoBack {
    override fun goBack() {}
}