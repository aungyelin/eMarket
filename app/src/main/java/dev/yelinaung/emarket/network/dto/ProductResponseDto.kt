package dev.yelinaung.emarket.network.dto

import com.google.gson.annotations.SerializedName

data class ProductResponseDto(
    @SerializedName("data") val data: ProductResponseDataDto
)
