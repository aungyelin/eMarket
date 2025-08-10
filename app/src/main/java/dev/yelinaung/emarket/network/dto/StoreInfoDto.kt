package dev.yelinaung.emarket.network.dto

import com.google.gson.annotations.SerializedName

data class StoreInfoDto(
    @SerializedName("name") val name: String,
    @SerializedName("rating") val rating: Double,
    @SerializedName("openingTime") val openingTime: String,
    @SerializedName("closingTime") val closingTime: String,
)
