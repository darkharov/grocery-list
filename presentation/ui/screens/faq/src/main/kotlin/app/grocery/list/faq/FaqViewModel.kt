package app.grocery.list.faq

import androidx.lifecycle.ViewModel
import app.grocery.list.domain.faq.FaqItemRepository
import app.grocery.list.faq.item.FaqItemMapper
import commons.android.customStateIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

@HiltViewModel
internal class FaqViewModel @Inject constructor(
    private val repository: FaqItemRepository,
    private val mapperFactory: FaqItemMapper.Factory,
) : ViewModel(),
    FaqCallbacks {

    private val selectedItemId = MutableStateFlow<Int?>(null)

    val props = props().customStateIn(this)

    private fun props(): Flow<FaqProps> =
        combine(itemMapper(), repository.all()) { itemMapper, items ->
            FaqProps(
                items = itemMapper.toPresentationList(items),
            )
        }

    private fun itemMapper(): Flow<FaqItemMapper> =
        selectedItemId
            .map { selectedItemId ->
                mapperFactory.create(selectedItemId = selectedItemId)
            }

    override fun onFaqItemClick(id: Int, expanded: Boolean) {
        selectedItemId.value = if (expanded) id else null
    }
}
