package app.grocery.list.commons.kotlin

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Suppress("UNCHECKED_CAST")
fun <T0, T1, T2, T3, T4, T5, R> customCombine(
    flow0: Flow<T0>,
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    transform: suspend (T0, T1, T2, T3, T4, T5) -> R,
): Flow<R> =
    combine(
        flow0,
        flow1,
        flow2,
        flow3,
        flow4,
        flow5,
    ) {
        array ->
        transform(
            array[0] as T0,
            array[1] as T1,
            array[2] as T2,
            array[3] as T3,
            array[4] as T4,
            array[5] as T5,
        )
    }
