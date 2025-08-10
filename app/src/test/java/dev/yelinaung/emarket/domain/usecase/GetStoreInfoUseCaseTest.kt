package dev.yelinaung.emarket.domain.usecase

import dev.yelinaung.emarket.domain.model.StoreInfo
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

class GetStoreInfoUseCaseTest {

    private lateinit var storeRepository: StoreRepository
    private lateinit var getStoreInfoUseCase: GetStoreInfoUseCase

    @Before
    fun setUp() {
        storeRepository = mockk()
        getStoreInfoUseCase = GetStoreInfoUseCase(storeRepository)
    }

    @Test
    fun `invoke should return success when repository call is successful`() = runTest {

        val fakeStoreInfo = StoreInfo(
            name = "The Coffee Shop",
            rating = 4.5,
            openingTime = "15:01:01.772Z",
            closingTime = "19:45:51.365Z"
        )
        coEvery { storeRepository.getStoreInfo() } returns Result.success(fakeStoreInfo)

        val result = getStoreInfoUseCase()

        assertTrue(result.isSuccess)
        assertEquals(fakeStoreInfo, result.getOrNull())
        coVerify(exactly = 1) { storeRepository.getStoreInfo() }

    }

    @Test
    fun `invoke should return failure when repository call fails`() = runTest {

        val exception = RuntimeException("Network error")
        coEvery { storeRepository.getStoreInfo() } returns Result.failure(exception)

        val result = getStoreInfoUseCase()

        assertTrue(result.isFailure)
        assertFalse(result.isSuccess)
        assertEquals(exception, result.exceptionOrNull())
        coVerify(exactly = 1) { storeRepository.getStoreInfo() }

    }

}