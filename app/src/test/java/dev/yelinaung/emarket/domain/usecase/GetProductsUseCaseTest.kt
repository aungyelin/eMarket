package dev.yelinaung.emarket.domain.usecase

import dev.yelinaung.emarket.domain.repository.StoreRepository
import dev.yelinaung.emarket.util.TestData
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetProductsUseCaseTest {

    private lateinit var storeRepository: StoreRepository
    private lateinit var getProductsUseCase: GetProductsUseCase

    @Before
    fun setUp() {
        storeRepository = mockk()
        getProductsUseCase = GetProductsUseCase(storeRepository)
    }

    @Test
    fun `invoke returns success when repository returns success`() = runTest {

        val expectedProducts = TestData.products
        coEvery { storeRepository.getProducts() } returns Result.success(expectedProducts)

        val result = getProductsUseCase()

        assertTrue(result.isSuccess)
        assertEquals(expectedProducts, result.getOrNull())

    }

    @Test
    fun `invoke returns failure when repository returns failure`() = runTest {

        val expectedException = RuntimeException("Network Error")
        coEvery { storeRepository.getProducts() } returns Result.failure(expectedException)

        val result = getProductsUseCase()

        assertTrue(result.isFailure)
        assertEquals(expectedException, result.exceptionOrNull())

    }

}