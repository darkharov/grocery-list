package app.grocery.list.notifications.internal.mapping

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GetGroupKeyOfProductCategory @Inject constructor() {

    fun execute(categoryId: Int): String =
        categoryId.toString()
}
