package dev.yelinaung.emarket.data.mapper

import dev.yelinaung.emarket.network.dto.ProductDto
import dev.yelinaung.emarket.domain.model.Product
import org.junit.Assert.assertEquals
import org.junit.Test

class ProductMapperTest {

    @Test
    fun `map ProductDto to Product`() {

        val productDto = ProductDto(
            name = "Apple",
            price = 1.0,
            imageUrl = "url_apple"
        )

        val product = productDto.toDomain()

        assertEquals("Apple", product.name)
        assertEquals(1.0, product.price, 0.0)
        assertEquals("url_apple", product.imageUrl)

    }

    @Test
    fun `map list of ProductDto to list of Product`() {

        val productDtos = listOf(
            ProductDto(
                name = "Apple",
                price = 1.0,
                imageUrl = "url_apple"
            ),
            ProductDto(
                name = "Banana",
                price = 2.0,
                imageUrl = "url_banana"
            )
        )


        val products = productDtos.map { it.toDomain() }

        assertEquals(2, products.size)
        assertEquals("Apple", products[0].name)
        assertEquals(1.0, products[0].price, 0.0)
        assertEquals("url_apple", products[0].imageUrl)
        assertEquals("Banana", products[1].name)
        assertEquals(2.0, products[1].price, 0.0)
        assertEquals("url_banana", products[1].imageUrl)

    }

    @Test
    fun `map Product to ProductDto`() {

        val product = Product(
            name = "Orange",
            price = 1.5,
            imageUrl = "url_orange"
        )

        val productDto = product.toDto()

        assertEquals("Orange", productDto.name)
        assertEquals(1.5, productDto.price, 0.0)
        assertEquals("url_orange", productDto.imageUrl)

    }

    @Test
    fun `map list of Product to list of ProductDto`() {

        val products = listOf(
            Product(
                name = "Orange",
                price = 1.5,
                imageUrl = "url_orange"
            ),
            Product(
                name = "Grape",
                price = 2.5,
                imageUrl = "url_grape"
            )
        )

        val productDtos = products.map { it.toDto() }

        assertEquals(2, productDtos.size)
        assertEquals("Orange", productDtos[0].name)
        assertEquals(1.5, productDtos[0].price, 0.0)
        assertEquals("url_orange", productDtos[0].imageUrl)
        assertEquals("Grape", productDtos[1].name)
        assertEquals(2.5, productDtos[1].price, 0.0)
        assertEquals("url_grape", productDtos[1].imageUrl)

    }

}
