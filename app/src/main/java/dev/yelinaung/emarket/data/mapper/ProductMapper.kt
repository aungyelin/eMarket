package dev.yelinaung.emarket.data.mapper

import dev.yelinaung.emarket.domain.model.Product
import dev.yelinaung.emarket.network.dto.ProductDto
import java.util.UUID

fun ProductDto.toDomain(): Product {
    return Product(
        name = name,
        price = price,
        imageUrl = imageUrl
    )
}

fun Product.toDto(): ProductDto {
    return ProductDto(
        name = name,
        price = price,
        imageUrl = imageUrl
    )
}