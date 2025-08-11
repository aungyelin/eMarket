package dev.yelinaung.emarket.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val name: String,
    val price: Double,
    val imageUrl: String,
)
