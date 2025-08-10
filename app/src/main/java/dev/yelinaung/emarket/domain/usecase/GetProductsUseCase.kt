package dev.yelinaung.emarket.domain.usecase

import dev.yelinaung.emarket.domain.model.Product
import dev.yelinaung.emarket.domain.repository.StoreRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(): Result<List<Product>> {
        return storeRepository.getProducts()
    }
}