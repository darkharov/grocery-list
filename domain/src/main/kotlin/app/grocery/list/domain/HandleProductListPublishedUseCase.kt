package app.grocery.list.domain

import app.grocery.list.domain.settings.BottomBarMode
import app.grocery.list.storage.value.kotlin.edit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HandleProductListPublishedUseCase @Inject internal constructor(
    private val repository: AppRepository,
) {
    suspend fun execute() {
        repository
            .bottomBarMode
            .edit { currentState ->
                if (currentState.isInitial) {
                    BottomBarMode.ShouldOfferToSwitchToIcons
                } else {
                    currentState
                }
            }
    }
}
