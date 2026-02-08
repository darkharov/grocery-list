package app.grocery.list.data.faq

import android.content.Context
import app.grocery.list.data.R
import app.grocery.list.domain.faq.FaqItem
import app.grocery.list.domain.faq.FaqItemRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Singleton
internal class FaqItemRepositoryImpl @Inject constructor(
    @param:ApplicationContext
    private val context: Context,
) : FaqItemRepository {

    override fun all(): Flow<List<FaqItem>> =
        flowOf(
            context
                .resources
                .getStringArray(R.array.faq)
                .mapIndexed { index, item ->
                    FaqItem(
                        id = index,
                        question = item.substringBefore('|'),
                        answer = item.substringAfter('|'),
                    )
                }
        )
}
