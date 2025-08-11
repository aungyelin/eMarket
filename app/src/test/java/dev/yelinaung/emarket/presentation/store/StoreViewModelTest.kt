package dev.yelinaung.emarket.presentation.store

import dev.yelinaung.emarket.domain.model.Product
import dev.yelinaung.emarket.domain.model.StoreInfo
import dev.yelinaung.emarket.domain.usecase.GetProductsUseCase
import dev.yelinaung.emarket.domain.usecase.GetStoreInfoUseCase
import dev.yelinaung.emarket.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class StoreViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getStoreInfoUseCase: GetStoreInfoUseCase = mockk()
    private val getProductsUseCase: GetProductsUseCase = mockk()

    private lateinit var viewModel: StoreViewModel

    @Test
    fun `init loads store data successfully`() = runTest {

        val fakeStoreInfo = StoreInfo(
            name = "Fruit Shop",
            rating = 4.5,
            openingTime = "15:01:01.772Z",
            closingTime = "19:45:51.365Z"
        )
        val fakeProducts = listOf(
            Product(name = "Apple", price = 10.0, imageUrl = "url_apple"),
            Product(name = "Banana", price = 20.0, imageUrl = "url_banana")
        )
        coEvery { getStoreInfoUseCase() } returns Result.success(fakeStoreInfo)
        coEvery { getProductsUseCase() } returns Result.success(fakeProducts)

        viewModel = StoreViewModel(getStoreInfoUseCase, getProductsUseCase)

        val uiState = viewModel.uiState.first()
        assertFalse(uiState.isLoading)
        assertEquals(fakeStoreInfo, uiState.storeInfo)
        assertEquals(2, uiState.products.size)
        assertEquals("Apple", uiState.products.first().product.name)
        assertNull(uiState.error)

    }

    @Test
    fun `init handles error when loading store info`() = runTest {

        val exception = RuntimeException("Store info error")
        coEvery { getStoreInfoUseCase() } returns Result.failure(exception)
        coEvery { getProductsUseCase() } returns Result.success(emptyList())

        viewModel = StoreViewModel(getStoreInfoUseCase, getProductsUseCase)

        val uiState = viewModel.uiState.first()
        assertFalse(uiState.isLoading)
        assertNull(uiState.storeInfo)
        assertTrue(uiState.products.isEmpty())
        assertEquals(exception, uiState.error)

    }

    @Test
    fun `init handles error when loading products`() = runTest {

        val fakeStoreInfo = StoreInfo(
            name = "Fruit Shop",
            rating = 4.5,
            openingTime = "15:01:01.772Z",
            closingTime = "19:45:51.365Z"
        )
        val exception = RuntimeException("Products error")
        coEvery { getStoreInfoUseCase() } returns Result.success(fakeStoreInfo)
        coEvery { getProductsUseCase() } returns Result.failure(exception)

        viewModel = StoreViewModel(getStoreInfoUseCase, getProductsUseCase)

        val uiState = viewModel.uiState.first()
        assertFalse(uiState.isLoading)
        assertNotNull(uiState.storeInfo)
        assertTrue(uiState.products.isEmpty())
        assertEquals(exception, uiState.error)

    }

    @Test
    fun `init handles error when both store info and products loading fail`() = runTest {

        val storeInfoException = RuntimeException("Store info error")
        val productsException = RuntimeException("Products error")

        coEvery { getStoreInfoUseCase() } returns Result.failure(storeInfoException)
        coEvery { getProductsUseCase() } returns Result.failure(productsException)

        viewModel = StoreViewModel(getStoreInfoUseCase, getProductsUseCase)

        val uiState = viewModel.uiState.first()
        assertFalse(uiState.isLoading)
        assertNull(uiState.storeInfo)
        assertTrue(uiState.products.isEmpty())
        assertEquals(productsException, uiState.error)

    }

    @Test
    fun `onProductSelected updates product selection`() = runTest {

        val fakeStoreInfo = StoreInfo(
            name = "Fruit Shop",
            rating = 4.5,
            openingTime = "15:01:01.772Z",
            closingTime = "19:45:51.365Z"
        )
        val fakeProducts = listOf(
            Product(name = "Apple", price = 10.0, imageUrl = "url_apple"),
            Product(name = "Banana", price = 20.0, imageUrl = "url_banana")
        )
        coEvery { getStoreInfoUseCase() } returns Result.success(fakeStoreInfo)
        coEvery { getProductsUseCase() } returns Result.success(fakeProducts)
        viewModel = StoreViewModel(getStoreInfoUseCase, getProductsUseCase)
        val initialProductUiModel = viewModel.uiState.first().products.first()

        viewModel.onProductSelected(initialProductUiModel)

        val updatedUiState = viewModel.uiState.first()
        assertTrue(updatedUiState.products.first().isSelected)

        viewModel.onProductSelected(updatedUiState.products.first())

        val finalUiState = viewModel.uiState.first()
        assertFalse(finalUiState.products.first().isSelected)

    }

    @Test
    fun `onQuantityChanged updates product quantity`() = runTest {

        val fakeStoreInfo = StoreInfo(
            name = "Fruit Shop",
            rating = 4.5,
            openingTime = "15:01:01.772Z",
            closingTime = "19:45:51.365Z"
        )
        val fakeProducts = listOf(
            Product(name = "Apple", price = 10.0, imageUrl = "url_apple"),
            Product(name = "Banana", price = 20.0, imageUrl = "url_banana")
        )
        coEvery { getStoreInfoUseCase() } returns Result.success(fakeStoreInfo)
        coEvery { getProductsUseCase() } returns Result.success(fakeProducts)
        viewModel = StoreViewModel(getStoreInfoUseCase, getProductsUseCase)
        val initialProductUiModel = viewModel.uiState.first().products.first()

        viewModel.onQuantityChanged(initialProductUiModel, 5)

        val updatedUiState = viewModel.uiState.first()
        assertEquals(5, updatedUiState.products.first().quantity)

    }

    @Test
    fun `onQuantityChanged does not update for quantity less than 1`() = runTest {

        val fakeStoreInfo = StoreInfo(
            name = "Fruit Shop",
            rating = 4.5,
            openingTime = "15:01:01.772Z",
            closingTime = "19:45:51.365Z"
        )
        val fakeProducts = listOf(
            Product(name = "Apple", price = 10.0, imageUrl = "url_apple"),
            Product(name = "Banana", price = 20.0, imageUrl = "url_banana")
        )
        coEvery { getStoreInfoUseCase() } returns Result.success(fakeStoreInfo)
        coEvery { getProductsUseCase() } returns Result.success(fakeProducts)
        viewModel = StoreViewModel(getStoreInfoUseCase, getProductsUseCase)
        val initialProductUiModel = viewModel.uiState.first().products.first()

        viewModel.onQuantityChanged(initialProductUiModel, 0)

        val updatedUiState = viewModel.uiState.first()
        assertEquals(initialProductUiModel.quantity, updatedUiState.products.first().quantity)
    }

    @Test
    fun `clearSelectionsAndRefresh resets selections and reloads data`() = runTest {

        val fakeStoreInfo = StoreInfo(
            name = "Fruit Shop",
            rating = 4.5,
            openingTime = "15:01:01.772Z",
            closingTime = "19:45:51.365Z"
        )
        val initialProduct = Product(name = "Apple", price = 10.0, imageUrl = "url_apple")
        val refreshedProduct = Product(name = "Banana", price = 20.0, imageUrl = "url_banana")

        var isInitCalled = false

        coEvery { getStoreInfoUseCase() } returns Result.success(fakeStoreInfo)
        coEvery { getProductsUseCase() } coAnswers {
            if (!isInitCalled) {
                isInitCalled = true
                Result.success(listOf(initialProduct))
            } else {
                Result.success(listOf(refreshedProduct))
            }
        }

        viewModel = StoreViewModel(getStoreInfoUseCase, getProductsUseCase)
        val productToSelect =
            viewModel.uiState.first().products.first { it.product.name == initialProduct.name }
        viewModel.onProductSelected(productToSelect)
        viewModel.onQuantityChanged(productToSelect.copy(isSelected = true), 3)

        val stateBeforeClear = viewModel.uiState.first()
        assertTrue(stateBeforeClear.products.first { it.product.name == initialProduct.name }.isSelected)
        assertEquals(
            3,
            stateBeforeClear.products.first { it.product.name == initialProduct.name }.quantity
        )

        viewModel.clearSelectionsAndRefresh()

        val refreshedUiState = viewModel.uiState.first()
        assertTrue(refreshedUiState.products.any { it.product.name == refreshedProduct.name })
        val newProductUiModel =
            refreshedUiState.products.first { it.product.name == refreshedProduct.name }
        assertFalse(newProductUiModel.isSelected)
        assertEquals(0, newProductUiModel.quantity)

        assertFalse(refreshedUiState.isLoading)
        assertNull(refreshedUiState.error)

    }

}
