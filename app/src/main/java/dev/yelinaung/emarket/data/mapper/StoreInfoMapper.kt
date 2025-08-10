package dev.yelinaung.emarket.data.mapper

import dev.yelinaung.emarket.domain.model.StoreInfo
import dev.yelinaung.emarket.network.dto.StoreInfoDto

fun StoreInfoDto.toDomain(): StoreInfo {
    return StoreInfo(
        name = name,
        rating = rating,
        openingTime = openingTime,
        closingTime = closingTime
    )
}