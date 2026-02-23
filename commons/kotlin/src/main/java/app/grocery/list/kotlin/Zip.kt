package app.grocery.list.kotlin

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Suppress("UNCHECKED_CAST")
fun <T0, T1, T2, T3, T4, T5, T6, T7, R> customCombine(
    flow0: Flow<T0>,
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    transform: suspend (T0, T1, T2, T3, T4, T5, T6, T7) -> R
): Flow<R> =
    combine(
        flow0,
        flow1,
        flow2,
        flow3,
        flow4,
        flow5,
        flow6,
        flow7,
        transform = { array ->
            transform(
                array[0] as T0,
                array[1] as T1,
                array[2] as T2,
                array[3] as T3,
                array[4] as T4,
                array[5] as T5,
                array[6] as T6,
                array[7] as T7,
            )
        }
    )

@Suppress("UNCHECKED_CAST")
fun <T0, T1, T2, T3, T4, T5, T6, R> customCombine(
    flow0: Flow<T0>,
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    transform: suspend (T0, T1, T2, T3, T4, T5, T6) -> R
): Flow<R> =
    combine(
        flow0,
        flow1,
        flow2,
        flow3,
        flow4,
        flow5,
        flow6,
        transform = { array ->
            transform(
                array[0] as T0,
                array[1] as T1,
                array[2] as T2,
                array[3] as T3,
                array[4] as T4,
                array[5] as T5,
                array[6] as T6,
            )
        }
    )
