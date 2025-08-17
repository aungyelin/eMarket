package dev.yelinaung.emarket.network.dto

import com.google.gson.annotations.SerializedName

data class ProductResultDto(
    @SerializedName("PaginationInfo") val paginationInfo: PaginationInfoDto,
    @SerializedName("Products") val products: List<ProductDto>
)
