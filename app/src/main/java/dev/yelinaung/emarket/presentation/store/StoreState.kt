package dev.yelinaung.emarket.presentation.store

import dev.yelinaung.emarket.domain.model.StoreInfo

data class StoreState(
    val isLoading: Boolean = false,
    val storeInfo: StoreInfo? = null,
    val products: List<ProductUiModel> = emptyList(),
    val error: Exception? = null
)