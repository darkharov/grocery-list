package app.grocery.list.faq

import androidx.compose.runtime.Stable
import app.grocery.list.faq.item.FaqItemCallbacks
import app.grocery.list.faq.item.FaqItemCallbacksMock

@Stable
internal interface FaqCallbacks :
    FaqItemCallbacks

internal object FaqCallbacksMock :
    FaqCallbacks,
    FaqItemCallbacks by FaqItemCallbacksMock
