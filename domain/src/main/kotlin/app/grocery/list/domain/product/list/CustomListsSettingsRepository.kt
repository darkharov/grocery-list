package app.grocery.list.domain.product.list

import kotlinx.coroutines.flow.Flow

interface CustomListsSettingsRepository {

    fun featureState(): Flow<CustomListsFeatureState>
    fun idOfSelectedCustomList(): Flow<Int?>

    suspend fun setFeatureEnabled(enabled: Boolean)
    suspend fun setSelectedCustomList(idOfCustomList: Int?)
}
