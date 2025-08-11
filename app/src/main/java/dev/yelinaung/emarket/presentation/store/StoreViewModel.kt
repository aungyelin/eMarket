package dev.yelinaung.emarket.presentation.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.yelinaung.emarket.domain.usecase.GetProductsUseCase
import dev.yelinaung.emarket.domain.usecase.GetStoreInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val getStoreInfoUseCase: GetStoreInfoUseCase,
    private val getProductsUseCase: GetProductsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(StoreState())
    val uiState: StateFlow<StoreState> = _uiState.asStateFlow()

    init {
        getStoreData()
    }

    fun getStoreData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val storeInfoResult = getStoreInfoUseCase.invoke()
            val productsResult = getProductsUseCase.invoke()

            var error: Exception? = null

            storeInfoResult.onFailure { error = it as Exception? }
            productsResult.onFailure { error = it as Exception? }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    storeInfo = storeInfoResult.getOrNull(),
                    products = (productsResult.getOrNull() ?: emptyList()).map { product ->
                        ProductUiModel(product = product)
                    },
                    error = error
                )
            }
        }
    }

    fun onProductSelected(product: ProductUiModel) {
        _uiState.update { state ->
            val updatedProducts = state.products.map {
                if (it.uuid == product.uuid) {
                    it.copy(isSelected = !it.isSelected)
                } else {
                    it
                }
            }
            state.copy(products = updatedProducts)
        }
    }

    fun onQuantityChanged(product: ProductUiModel, quantity: Int) {
        if (quantity > 0) {
            _uiState.update { state ->
                val updatedProducts = state.products.map {
                    if (it.uuid == product.uuid) {
                        it.copy(quantity = quantity)
                    } else {
                        it
                    }
                }
                state.copy(products = updatedProducts)
            }
        }
    }

    fun clearSelectionsAndRefresh() {
        _uiState.update { state ->
            val updatedProducts = state.products.map {
                it.copy(isSelected = false, quantity = 0)
            }
            state.copy(products = updatedProducts)
        }
        getStoreData()
    }

}
