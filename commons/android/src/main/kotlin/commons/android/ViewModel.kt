package commons.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

fun <T : Any> Flow<T?>.stateIn(
    viewModel: ViewModel,
): StateFlow<T?> =
    stateIn(
        scope = viewModel.viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null,
    )

fun <T : Any> Flow<T>.stateIn(
    viewModel: ViewModel,
    defaultValue: T,
): StateFlow<T> =
    stateIn(
        scope = viewModel.viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = defaultValue,
    )
