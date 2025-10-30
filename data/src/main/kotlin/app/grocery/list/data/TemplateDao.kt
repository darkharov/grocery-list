package app.grocery.list.data

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import app.grocery.list.domain.list.preview.Template
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TemplateDao @Inject constructor(
    @ApplicationContext
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
        FruitsAndHoney(
            titleId = R.string.fruits_and_honey,
            productTitles = R.array.fruits_and_honey,
        ),
        GreekSalad(
            titleId = R.string.greek_salad,
            productTitles = R.array.greek_salad,
        ),
        CesarSaladWithChicken(
            titleId = R.string.ceaser_salad_with_chicken,
            productTitles = R.array.ceaser_salad_with_chicken,
        ),
        ;
    }
}
