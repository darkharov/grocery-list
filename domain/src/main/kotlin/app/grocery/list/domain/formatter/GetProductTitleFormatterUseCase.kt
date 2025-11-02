package app.grocery.list.domain.formatter

import app.grocery.list.domain.settings.ProductTitleFormat
import app.grocery.list.domain.settings.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
internal class GetProductTitleFormatterUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    fun execute(): Flow<Result> =
        settingsRepository
            .productTitleFormat
            .observe()
            .map {
                Result(
                    format = it,
                    formatter = formatter(it),
                )
            }

    private fun formatter(it: ProductTitleFormat) =
        when (it) {
            ProductTitleFormat.WithoutEmoji -> {
                ProductTitleWithoutEmojiFormatter
            }
            ProductTitleFormat.EmojiAndFullText -> {
                ProductEmojiAndFullTextFormatter
            }
            ProductTitleFormat.EmojiAndAdditionalDetails -> {
                ProductEmojiAndAdditionalDetailsFormatter
            }
        }

    data class Result(
        val format: ProductTitleFormat,
        val formatter: ProductTitleFormatter,
    )
}
