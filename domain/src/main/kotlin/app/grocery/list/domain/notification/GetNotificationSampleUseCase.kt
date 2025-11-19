package app.grocery.list.domain.notification

import app.grocery.list.domain.formatter.GetProductTitleFormatterUseCase
import app.grocery.list.domain.product.ProductRepository
import app.grocery.list.domain.settings.ProductTitleFormat
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class GetNotificationSampleUseCase @Inject internal constructor(
    private val productRepository: ProductRepository,
    private val getTitleFormatter: GetProductTitleFormatterUseCase,
    private val formatNotificationTitle: FormatNotificationTitleUseCase,
) {
    fun execute(): Flow<Result> =
        combine(
            getTitleFormatter.execute(),
            productRepository.samples(),
        ) { formatterResult, products ->
            Result(
                format = formatterResult
                    .format,
                notificationText = formatNotificationTitle
                    .execute(
                        formatter = formatterResult.formatter,
                        products = products,
                    ),
            )
        }

    class Result(
        val format: ProductTitleFormat,
        val notificationText: String,
    )
}
