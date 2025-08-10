package dev.yelinaung.emarket.network.dto

import com.google.gson.annotations.SerializedName

data class OrderDto(
    @SerializedName("products") val products: List<ProductDto>,
    @SerializedName("delivery_address") val deliveryAddress: String,
)
