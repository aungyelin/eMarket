package dev.yelinaung.emarket.network.dto

import com.google.gson.annotations.SerializedName

data class PaginationInfoDto(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("total_pages") val totalPages: Int,
)
