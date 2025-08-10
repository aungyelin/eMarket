package dev.yelinaung.emarket.domain.repository

import dev.yelinaung.emarket.domain.model.Product
import dev.yelinaung.emarket.domain.model.StoreInfo

interface StoreRepository {
    suspend fun getStoreInfo(): Result<StoreInfo>
    suspend fun getProducts(): Result<List<Product>>
    suspend fun orderProduct(product: Product): Result<Unit>
}