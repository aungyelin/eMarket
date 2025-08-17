package dev.yelinaung.emarket.network.dto

import com.google.gson.annotations.SerializedName

data class ProductResponseDataDto(
    @SerializedName("ProductResult") val productResult: ProductResultDto
)
