package dev.yelinaung.emarket.presentation.summary

import dev.yelinaung.emarket.domain.model.Product
import dev.yelinaung.emarket.domain.usecase.OrderProductUseCase
import dev.yelinaung.emarket.presentation.store.ProductUiModel
import dev.yelinaung.emarket.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SummaryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var orderProductUseCase: OrderProductUseCase
    private lateinit var viewModel: SummaryViewModel

    @Before
    fun setUp() {
        orderProductUseCase = mockk()
        viewModel = SummaryViewModel(orderProductUseCase)
    }

    @Test
    fun `order success updates uiState to Success`() = runTest {

        val fakeProducts = listOf(
            Product(name = "Apple", price = 10.0, imageUrl = "url_apple"),
            Product(name = "Banana", price = 20.0, imageUrl = "url_banana")
        ).map {
            ProductUiModel(product = it, quantity = 2, isSelected = true)
        }
        val fakeAddress = "Test Address"
        coEvery { orderProductUseCase(any(), any()) } returns Result.success(Unit)

        viewModel.order(fakeProducts, fakeAddress)

        val uiState = viewModel.uiState.first()
        if (uiState is SummaryUiState.Loading) {
            assertEquals(SummaryUiState.Success, viewModel.uiState.first())
        } else {
            assertEquals(SummaryUiState.Success, uiState)
        }

        coVerify { orderProductUseCase(fakeProducts.map { it.product }, fakeAddress) }

    }

    @Test
    fun `order failure updates uiState to Error`() = runTest {

        val fakeProducts = listOf(
            Product(name = "Apple", price = 10.0, imageUrl = "url_apple"),
            Product(name = "Banana", price = 20.0, imageUrl = "url_banana")
        ).map {
            ProductUiModel(product = it, quantity = 2, isSelected = true)
        }
        val fakeAddress = "Test Address"
        val errorMessage = "Order failed"
        val exception = RuntimeException(errorMessage)
        coEvery { orderProductUseCase(any(), any()) } returns Result.failure(exception)

        viewModel.order(fakeProducts, fakeAddress)

        val uiState = viewModel.uiState.first()
        val finalState = if (uiState is SummaryUiState.Loading) {
            viewModel.uiState.first()
        } else {
            uiState
        }
        assertTrue(finalState is SummaryUiState.Error)
        assertEquals(errorMessage, (finalState as SummaryUiState.Error).message)
        coVerify { orderProductUseCase(fakeProducts.map { it.product }, fakeAddress) }

    }

    @Test
    fun `order emits Loading state before success`() = runTest {

        val fakeProducts = listOf(
            Product(name = "Apple", price = 10.0, imageUrl = "url_apple"),
            Product(name = "Banana", price = 20.0, imageUrl = "url_banana")
        ).map {
            ProductUiModel(product = it, quantity = 2, isSelected = true)
        }
        val fakeAddress = "Test Address"
        coEvery { orderProductUseCase(any(), any()) } coAnswers {
            assertEquals(SummaryUiState.Loading, viewModel.uiState.value)
            Result.success(Unit)
        }

        viewModel.order(fakeProducts, fakeAddress)

        assertEquals(SummaryUiState.Success, viewModel.uiState.value)

    }

    @Test
    fun `order emits Loading state before error`() = runTest {

        val fakeProducts = listOf(
            Product(name = "Apple", price = 10.0, imageUrl = "url_apple"),
            Product(name = "Banana", price = 20.0, imageUrl = "url_banana")
        ).map {
            ProductUiModel(product = it, quantity = 2, isSelected = true)
        }
        val fakeAddress = "Test Address"
        val errorMessage = "Order failed"
        val exception = RuntimeException(errorMessage)
        coEvery { orderProductUseCase(any(), any()) } coAnswers {
            assertEquals(SummaryUiState.Loading, viewModel.uiState.value)
            Result.failure(exception)
        }

        viewModel.order(fakeProducts, fakeAddress)

        val finalState = viewModel.uiState.value
        assertTrue(finalState is SummaryUiState.Error)
        assertEquals(errorMessage, (finalState as SummaryUiState.Error).message)

    }


    @Test
    fun `resetUiState updates uiState to Idle`() = runTest {

        val fakeProducts = listOf(
            Product(name = "Apple", price = 10.0, imageUrl = "url_apple"),
            Product(name = "Banana", price = 20.0, imageUrl = "url_banana")
        ).map {
            ProductUiModel(product = it, quantity = 2, isSelected = true)
        }
        val fakeAddress = "Test Address"
        coEvery { orderProductUseCase(any(), any()) } returns Result.success(Unit)
        viewModel.order(fakeProducts, fakeAddress)
        assertEquals(SummaryUiState.Success, viewModel.uiState.first())

        viewModel.resetUiState()

        assertEquals(SummaryUiState.Idle, viewModel.uiState.first())

    }

}
