package dev.yelinaung.emarket.data.mapper

import dev.yelinaung.emarket.domain.model.Product
import dev.yelinaung.emarket.network.dto.ProductDto

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        price = price,
        imageUrl = imageUrl
    )
}

fun Product.toDto(): ProductDto {
    return ProductDto(
        id = id,
        name = name,
        price = price,
        imageUrl = imageUrl
    )
}