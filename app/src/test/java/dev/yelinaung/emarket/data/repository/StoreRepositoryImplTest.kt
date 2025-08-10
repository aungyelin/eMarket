package dev.yelinaung.emarket.data.repository

import dev.yelinaung.emarket.data.mapper.toDto
import dev.yelinaung.emarket.domain.model.DataException
import dev.yelinaung.emarket.domain.model.Product
import dev.yelinaung.emarket.network.ApiService
import dev.yelinaung.emarket.network.dto.OrderDto
import dev.yelinaung.emarket.network.dto.ProductDto
import dev.yelinaung.emarket.network.dto.StoreInfoDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class StoreRepositoryImplTest {

    private lateinit var apiService: ApiService
    private lateinit var storeRepository: StoreRepositoryImpl

    @Before
    fun setUp() {
        apiService = mockk(relaxed = true)
        storeRepository = StoreRepositoryImpl(apiService)
    }

    @Test
    fun `getStoreInfo success should return store info`() = runBlocking {

        val storeInfoDto = StoreInfoDto(
            "My Store",
            4.5,
            "15:01:01.772Z",
            "19:45:51.365Z"
        )
        coEvery { apiService.getStoreInfo() } returns storeInfoDto

        val result = storeRepository.getStoreInfo()

        assertTrue(result.isSuccess)
        assertEquals("My Store", result.getOrNull()?.name)
        coVerify(exactly = 1) { apiService.getStoreInfo() }

    }

    @Test
    fun `getStoreInfo network error should return network exception`() = runBlocking {

        coEvery { apiService.getStoreInfo() } throws IOException()

        val result = storeRepository.getStoreInfo()

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is DataException.Network)

    }

    @Test
    fun `getProducts success should return product list`() = runBlocking {

        val productDtos = listOf(ProductDto("Apple", 1.0, "url_apple"))
        coEvery { apiService.getProducts() } returns productDtos

        val result = storeRepository.getProducts()

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertEquals("Apple", result.getOrNull()?.first()?.name)
        coVerify(exactly = 1) { apiService.getProducts() }

    }

    @Test
    fun `getProducts network error should return network exception`() = runBlocking {

        coEvery { apiService.getProducts() } throws IOException()

        val result = storeRepository.getProducts()

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is DataException.Network)

    }

    @Test
    fun `orderProduct success should complete without error`() = runBlocking {

        val products = listOf(Product("Banana", 0.6, "url_banana"))
        val address = "123 Main St"
        val orderDto = OrderDto(products.map { it.toDto() }, address)

        coEvery { apiService.postOrder(orderDto) } returns Unit

        val result = storeRepository.orderProduct(products, address)

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { apiService.postOrder(orderDto) }

    }

    @Test
    fun `orderProduct network error should return network exception`() = runBlocking {

        val products = listOf(Product("Banana", 0.6, "url_banana"))
        val address = "123 Main St"
        val orderDto = OrderDto(products.map { it.toDto() }, address)

        coEvery { apiService.postOrder(orderDto) } throws IOException()

        val result = storeRepository.orderProduct(products, address)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is DataException.Network)

    }

}
