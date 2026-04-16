package app.grocery.list.data.product.list.setting

import app.grocery.list.domain.product.list.CustomProductListsSetting
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CustomProductListsSettingMapper @Inject constructor() {

    fun toDomain(value: Int): CustomProductListsSetting =
        when (value) {
            NOT_SET -> CustomProductListsSetting.NotSet
            ENABLED -> CustomProductListsSetting.Enabled
            DISABLED -> CustomProductListsSetting.Disabled
            else -> throw IllegalArgumentException("Unknown constant: $value")
        }

    fun toData(setting: CustomProductListsSetting): Int =
        when (setting) {
            CustomProductListsSetting.NotSet -> NOT_SET
            CustomProductListsSetting.Enabled -> ENABLED
            CustomProductListsSetting.Disabled -> DISABLED
        }

    companion object {
        private const val NOT_SET = 0
        private const val ENABLED = 1
        private const val DISABLED = 2
    }
}
