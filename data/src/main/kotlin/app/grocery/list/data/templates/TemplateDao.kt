package app.grocery.list.data.templates

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import app.grocery.list.data.R
import app.grocery.list.domain.template.Template
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TemplateDao @Inject constructor(
    @param:ApplicationContext
    private val context: Context,
) {
    fun templates(): List<Template> =
        TemplateKey
            .entries
            .map {
                Template(
                    id = it.ordinal,
                    title = context.getString(it.titleId),
                )
            }

    fun productTitles(templateId: Int): List<String> {
        val arrayId = TemplateKey.entries[templateId].productTitles
        return context.resources.getStringArray(arrayId).toList()
    }

    enum class TemplateKey(
        @StringRes
        val titleId: Int,
        @ArrayRes
        val productTitles: Int,
    ) {
        CesarSaladWithChicken(
            titleId = R.string.ceaser_salad_with_chicken,
            productTitles = R.array.ceaser_salad_with_chicken,
        ),
        GreekSalad(
            titleId = R.string.greek_salad,
            productTitles = R.array.greek_salad,
        ),
        FruitsAndHoney(
            titleId = R.string.berries_fruits_and_honey,
            productTitles = R.array.berries_fruits_and_honey,
        ),
        ;
    }
}
