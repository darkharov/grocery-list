package app.grocery.list.faq.item

import androidx.compose.runtime.Stable

@Stable
internal interface FaqItemCallbacks {
    fun onFaqItemClick(id: Int, expanded: Boolean)
}

internal object FaqItemCallbacksMock : FaqItemCallbacks {
    override fun onFaqItemClick(id: Int, expanded: Boolean) {}
}
