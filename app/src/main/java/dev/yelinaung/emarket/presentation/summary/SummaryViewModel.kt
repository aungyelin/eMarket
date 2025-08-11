package dev.yelinaung.emarket.presentation.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.yelinaung.emarket.domain.usecase.OrderProductUseCase
import dev.yelinaung.emarket.presentation.store.ProductUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val orderProductUseCase: OrderProductUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SummaryUiState>(SummaryUiState.Idle)
    val uiState: StateFlow<SummaryUiState> = _uiState.asStateFlow()

    fun order(
        products: List<ProductUiModel>,
        address: String,
    ) {
        viewModelScope.launch {
            _uiState.value = SummaryUiState.Loading
            orderProductUseCase(products.map { it.product }, address)
                .onSuccess {
                    _uiState.value = SummaryUiState.Success
                }
                .onFailure { throwable ->
                    _uiState.value = SummaryUiState.Error(throwable.message ?: "An unexpected error occurred")
                }
        }
    }

    fun resetUiState() {
        _uiState.value = SummaryUiState.Idle
    }

}
