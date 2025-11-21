package app.grocery.list.settings.use.icons.on.bottom.bar.switch_

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.domain.settings.BottomBarRoadmapStep
import app.grocery.list.domain.settings.SettingsRepository
import app.grocery.list.settings.use.icons.on.bottom.bar.switch_.UseIconsOnBottomBarSwitchStrategy.EmbeddedElement
import app.grocery.list.settings.use.icons.on.bottom.bar.switch_.UseIconsOnBottomBarSwitchStrategy.Screen
import commons.android.customStateIn
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel(
    assistedFactory = UseIconsOnBottomBarSwitchViewModel.Factory::class,
)
internal class UseIconsOnBottomBarSwitchViewModel @AssistedInject constructor(
    @Assisted
    private val strategy: UseIconsOnBottomBarSwitchStrategy,
    private val repository: SettingsRepository,
) : ViewModel(),
    UseIconsOnBottomBarSwitchCallbacks {

    private val events = Channel<Event>(capacity = Channel.UNLIMITED)

    val props =
        repository
            .bottomBarRoadmapStep
            .observe()
            .map { step ->
                UseIconsOnBottomBarSwitchProps(
                    checked = step.useIcons,
                    visible = when (strategy) {
                        Screen -> {
                            true
                        }
                        EmbeddedElement -> {
                            step.shouldOfferToSwitchToIcons
                        }
                    },
                    strategy = strategy,
                )
            }
            .customStateIn(
                viewModel = this,
                defaultValue = UseIconsOnBottomBarSwitchProps(
                    checked = null,
                    visible = false,
                    strategy = strategy,
                ),
            )

    init {
        if (strategy.shouldExitIfToggledOn) {
            viewModelScope.launch {
                repository
                    .bottomBarRoadmapStep
                    .observe()
                    .first { it.useIcons }
                events.trySend(Event.OnGoBack)
            }
        }
    }

    override fun onClose() {    // user rejected the offer to switch to icons
        viewModelScope.launch {
            repository.bottomBarRoadmapStep.set(BottomBarRoadmapStep.ButtonsIsExplicitlySelected)
        }
    }

    override fun onUseIconsOnBottomBarCheckedChange(newValue: Boolean) {
        viewModelScope.launch {
            repository.bottomBarRoadmapStep.set(
                if (newValue) {
                    BottomBarRoadmapStep.IconsModeIsExplicitlySelected
                } else {
                    BottomBarRoadmapStep.ButtonsIsExplicitlySelected
                }
            )
        }
        if (newValue && strategy.shouldExitIfToggledOn) {
            events.trySend(Event.OnGoBack)
        }
    }

    fun events(): ReceiveChannel<Event> =
        events

    @AssistedFactory
    fun interface Factory {
        fun create(strategy: UseIconsOnBottomBarSwitchStrategy): UseIconsOnBottomBarSwitchViewModel
    }

    sealed class Event {
        data object OnGoBack : Event()
    }
}
