package app.grocery.list.assembly.impls

import android.content.Context
import app.grocery.list.assembly.R
import app.grocery.list.domain.product.EmojiAndCategoryId
import app.grocery.list.domain.product.ProductRepository
import app.grocery.list.domain.sharing.SharingStringFormatter
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharingStringFormatterContractImpl @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val repository: ProductRepository,
) : SharingStringFormatter.Contract {

    override fun recommendationToUseThisApp(): String =
        with(context) {
            getString(
                R.string.recommendation_to_use_this_app,
                getString(R.string.paste_copied_list),
                getString(R.string.actions),
                "https://play.google.com/store/apps/details?id=app.grocery.list",
            )
        }

    override suspend fun findEmojiAndCategoryId(search: String): EmojiAndCategoryId =
        repository.findEmojiAndCategoryId(search = search)
}
