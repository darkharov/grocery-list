package app.grocery.list.domain.format.sharing

import app.grocery.list.domain.product.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShareProductListUseCase @Inject internal constructor(
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
