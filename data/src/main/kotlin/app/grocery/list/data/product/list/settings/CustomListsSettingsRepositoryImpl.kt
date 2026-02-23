package app.grocery.list.data.product.list.settings

import app.grocery.list.domain.product.list.CustomListsFeatureState
import app.grocery.list.domain.product.list.CustomListsSettingsRepository
import app.grocery.list.storage.value.android.StorageValueFactory
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
internal class CustomListsSettingsRepositoryImpl @Inject constructor(
    storageValueFactory: StorageValueFactory,
) : CustomListsSettingsRepository {

    private val state = storageValueFactory.enum(STATE, CustomListsFeatureState.NotSet)
    private val idOfSelectedCustomList = storageValueFactory.nullableInt(ID_OF_SELECTED_CUSTOM_LIST)

    override fun featureState(): Flow<CustomListsFeatureState> =
        state.observe()

    override fun idOfSelectedCustomList(): Flow<Int?> =
        idOfSelectedCustomList.observe()

    override suspend fun setFeatureEnabled(enabled: Boolean) {
        state.set(
            if (enabled) {
                CustomListsFeatureState.Enabled
            } else {
                CustomListsFeatureState.Disabled
            }
        )
    }

    override suspend fun setSelectedCustomList(idOfCustomList: Int?) {
        idOfSelectedCustomList.set(idOfCustomList)
    }

    private companion object {
        private const val STATE = "app.grocery.list.data.product.list.STATE"
        private const val ID_OF_SELECTED_CUSTOM_LIST = "app.grocery.list.data.product.list.ID_OF_SELECTED_CUSTOM_LIST"
    }
}
