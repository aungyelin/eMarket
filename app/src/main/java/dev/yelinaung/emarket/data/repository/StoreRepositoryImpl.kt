package dev.yelinaung.emarket.data.repository

import dev.yelinaung.emarket.data.mapper.mapNetworkErrorToDataException
import dev.yelinaung.emarket.data.mapper.toDomain
import dev.yelinaung.emarket.data.mapper.toDto
import dev.yelinaung.emarket.domain.model.Product
import dev.yelinaung.emarket.domain.model.StoreInfo
import dev.yelinaung.emarket.domain.repository.StoreRepository
import dev.yelinaung.emarket.network.ApiService
import dev.yelinaung.emarket.network.dto.OrderDto
import dev.yelinaung.emarket.network.safeApiCall
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : StoreRepository {

    override suspend fun getStoreInfo(): Result<StoreInfo> {
        return safeApiCall {
            apiService.getStoreInfo().toDomain()
        }.mapNetworkErrorToDataException()
    }

    override suspend fun getProducts(): Result<List<Product>> {
        return safeApiCall {
            apiService.getProducts().map { it.toDomain() }
        }.mapNetworkErrorToDataException()
    }

    override suspend fun orderProduct(product: List<Product>, address: String): Result<Unit> {
        return safeApiCall {
            apiService.postOrder(
                OrderDto(
                    products = product.map { it.toDto() },
                    deliveryAddress = address,
                )
            )
        }.mapNetworkErrorToDataException()
    }

}
