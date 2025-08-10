package dev.yelinaung.emarket.domain.usecase

import dev.yelinaung.emarket.domain.model.Product
import dev.yelinaung.emarket.domain.repository.StoreRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class OrderProductUseCaseTest {

    private lateinit var storeRepository: StoreRepository
    private lateinit var orderProductUseCase: OrderProductUseCase

    @Before
    fun setUp() {
        storeRepository = mockk()
        orderProductUseCase = OrderProductUseCase(storeRepository)
    }

    @Test
    fun `invoke should return success when repository call is successful`() = runTest {

        val fakeProduct = Product(name = "Test Product", price = 10.0, imageUrl = "")
        coEvery { storeRepository.orderProduct(fakeProduct) } returns Result.success(Unit)

        val result = orderProductUseCase(fakeProduct)

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { storeRepository.orderProduct(fakeProduct) }

    }

    @Test
    fun `invoke should return failure when repository call fails`() = runTest {

        val fakeProduct = Product(name = "Another Product", price = 20.0, imageUrl = "")
        val exception = RuntimeException("Network error")
        coEvery { storeRepository.orderProduct(fakeProduct) } returns Result.failure(exception)

        val result = orderProductUseCase(fakeProduct)

        assertTrue(result.isFailure)
        assertFalse(result.isSuccess)
        assertEquals(exception, result.exceptionOrNull())
        coVerify(exactly = 1) { storeRepository.orderProduct(fakeProduct) }

    }

}