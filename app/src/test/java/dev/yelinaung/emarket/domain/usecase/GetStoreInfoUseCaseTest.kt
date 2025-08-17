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

        val fakeStoreInfo = TestData.storeInfo
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