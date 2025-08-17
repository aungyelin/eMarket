package dev.yelinaung.emarket.presentation.store

import dev.yelinaung.emarket.domain.model.Product
import kotlinx.serialization.Serializable

@Serializable
data class ProductUiModel(
    val product: Product,
    var isSelected: Boolean = false,
    var quantity: Int = 0,
)