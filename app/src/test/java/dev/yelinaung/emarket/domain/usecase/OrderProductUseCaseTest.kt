package dev.yelinaung.emarket.domain.usecase

import dev.yelinaung.emarket.domain.repository.StoreRepository
import dev.yelinaung.emarket.util.TestData
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

        val fakeAddress = "123 Main St"
        val fakeProducts = TestData.products.subList(0, 2)

        coEvery {
            storeRepository.orderProduct(fakeProducts, fakeAddress)
        } returns Result.success(Unit)

        val result = orderProductUseCase(fakeProducts, fakeAddress)

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { storeRepository.orderProduct(fakeProducts, fakeAddress) }

    }

    @Test
    fun `invoke should return failure when repository call fails`() = runTest {

        val fakeAddress = "123 Main St"
        val fakeProducts = TestData.products.subList(0, 2)

        val exception = RuntimeException("Network error")
        coEvery {
            storeRepository.orderProduct(fakeProducts, fakeAddress)
        } returns Result.failure(exception)

        val result = orderProductUseCase(fakeProducts, fakeAddress)

        assertTrue(result.isFailure)
        assertFalse(result.isSuccess)
        assertEquals(exception, result.exceptionOrNull())
        coVerify(exactly = 1) { storeRepository.orderProduct(fakeProducts, fakeAddress) }

    }

}