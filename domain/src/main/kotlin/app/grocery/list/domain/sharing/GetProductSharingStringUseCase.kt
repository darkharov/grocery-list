package app.grocery.list.domain.sharing

import app.grocery.list.domain.product.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetProductSharingStringUseCase @Inject internal constructor(
    private val sharingStringFormatter: SharingStringFormatter,
) {
    fun execute(
        products: List<Product>,
        recommendUsingThisApp: Boolean,
    ): String =
        sharingStringFormatter.toSharingString(
            products = products,
            recommendUsingThisApp = recommendUsingThisApp,
        )
}
