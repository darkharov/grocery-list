package app.grocery.list.custom.product.lists.input.form

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.placeCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.commons.compose.elements.color.scheme.AppColorSchemeProps
import app.grocery.list.commons.compose.elements.color.scheme.ColorSchemeMapper
import app.grocery.list.domain.product.list.ProductList
import app.grocery.list.domain.product.list.ProductListRepository
import app.grocery.list.domain.theming.ColorScheme
import commons.android.customStateIn
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel(
    assistedFactory = CustomListInputFormViewModel.Factory::class,
)
internal class CustomListInputFormViewModel @AssistedInject constructor(
    @Assisted
    private val id: ProductList.Id.Custom?,
    private val colorSchemeMapper: ColorSchemeMapper,
    private val customListInputFormMapper: CustomListInputFormMapper,
    private val productListRepository: ProductListRepository,
) : ViewModel(),
    CustomListInputFormCallbacks {

    private val events = Channel<Event>(capacity = Channel.UNLIMITED)
    private val initial = MutableStateFlow<ProductList?>(null)
    private val colorScheme = MutableStateFlow<ColorScheme?>(null)

    val title = TextFieldState()

    val props: StateFlow<CustomListInputFormProps?> =
        combine(
            initial,
            snapshotFlow { title.text.toString() },
            colorScheme.filterNotNull(),
            CustomListInputFormMapper::Params,
        ).map(customListInputFormMapper::transform)
            .customStateIn(this)

    @Inject
    fun assignInitialValues() {
        if (id != null) {
            viewModelScope.launch {
                val list = productListRepository.get(id)
                initial.value = list
                title.edit {
                    append(list.title)
                    placeCursorAtEnd()
                }
                colorScheme.value = list.colorScheme
            }
        } else {
            colorScheme.value = ColorScheme.Default
        }
    }

    override fun onColorSchemeSelectionChange(newValue: AppColorSchemeProps) {
        colorScheme.value = colorSchemeMapper.toDomain(newValue)
    }

    override fun onComplete(title: String, colorScheme: AppColorSchemeProps) {
        viewModelScope.launch {
            productListRepository.put(
                ProductList.PutParams(
                    customListId = id,
                    title = title
                        .trim()
                        .replaceFirstChar { it.uppercase() },
                    colorScheme = colorSchemeMapper.toDomain(colorScheme),
                )
            )
            events.trySend(Event.OnExit)
        }
    }

    fun events(): ReceiveChannel<Event> =
        events

    sealed class Event {
        data object OnExit : Event()
    }

    @AssistedFactory
    fun interface Factory {
        fun create(customListId: ProductList.Id.Custom?): CustomListInputFormViewModel
    }
}
