package dev.yelinaung.emarket.network

import dev.yelinaung.emarket.network.dto.OrderDto
import dev.yelinaung.emarket.network.dto.ProductDto
import dev.yelinaung.emarket.network.dto.StoreInfoDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("/storeInfo")
    suspend fun getStoreInfo(): StoreInfoDto

    @GET("/products")
    suspend fun getProducts(): List<ProductDto>

    @POST("/order")
    suspend fun postOrder(@Body order: OrderDto)

}