package app.grocery.list.assembly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.domain.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MainViewModel @Inject constructor(
    repository: AppRepository,
) : ViewModel() {

    val numberOfAddedProduct =
        repository
            .getNumberOfAddedProducts()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val productList =
        repository
            .getProductList()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}
