package app.grocery.list.domain

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.takeWhile

@Singleton
class AtLeastOneProductJustAddedUseCase @Inject constructor(
    private val appRepository: AppRepository,
) {
    fun execute(): Flow<Boolean> =
        appRepository
            .numberOfProducts()
            .runningFold(Accumulator(), Accumulator::next)
            .map { it.increased }
            .takeWhile { increased -> !(increased) }
            .onCompletion { emit(true) }

    private data class Accumulator(
        val increased: Boolean = false,
        val numberOfAddedProducts: Int? = null,
    ) {
        fun next(newNumberOfAddedProducts: Int) =
            Accumulator(
                increased = increased ||
                (numberOfAddedProducts != null && newNumberOfAddedProducts > numberOfAddedProducts),
                numberOfAddedProducts = newNumberOfAddedProducts,
            )
    }
}
