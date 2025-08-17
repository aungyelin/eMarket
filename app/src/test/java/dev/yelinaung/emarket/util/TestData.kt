package dev.yelinaung.emarket.util

import dev.yelinaung.emarket.domain.model.Product
import dev.yelinaung.emarket.domain.model.StoreInfo
import dev.yelinaung.emarket.network.dto.PaginationInfoDto
import dev.yelinaung.emarket.network.dto.ProductDto
import dev.yelinaung.emarket.network.dto.ProductResponseDataDto
import dev.yelinaung.emarket.network.dto.ProductResponseDto
import dev.yelinaung.emarket.network.dto.ProductResultDto
import dev.yelinaung.emarket.network.dto.StoreInfoDto

object TestData {

    val storeInfoResponse = StoreInfoDto(
        name = "The Coffee Shop",
        rating = 4.5,
        openingTime = "15:01:01.772Z",
        closingTime = "19:45:51.365Z"
    )

    val storeInfo = StoreInfo(
        name = "The Coffee Shop",
        rating = 4.5,
        openingTime = "15:01:01.772Z",
        closingTime = "19:45:51.365Z"
    )

    val productResponse = ProductResponseDto(
        data = ProductResponseDataDto(
            productResult = ProductResultDto(
                paginationInfo = PaginationInfoDto(
                    totalCount = 10,
                    currentPage = 1,
                    totalPages = 2
                ),
                products = listOf(
                    ProductDto(
                        id = 1,
                        name = "Latte",
                        price = 95.0,
                        imageUrl = "https://www.nespresso.com/ncp/res/uploads/recipes/nespresso-recipes-Latte-Art-Tulip.jpg"
                    ),
                    ProductDto(
                        id = 2,
                        name = "Dark Tiramisu Mocha",
                        price = 95.0,
                        imageUrl = "https://www.nespresso.com/shared_res/mos/free_html/sg/b2b/b2ccoffeerecipes/listing-image/image/dark-tiramisu-mocha.jpg"
                    ),
                    ProductDto(
                        id = 3,
                        name = "Tropical Iced Rio",
                        price = 110.0,
                        imageUrl = "https://www.nespresso.com/ecom/medias/sys_master/public/15476364836894/tropical-iced-rio.jpg"
                    ),
                    ProductDto(
                        id = 4,
                        name = "Vanilla Iced Matcha Latte",
                        price = 135.0,
                        imageUrl = "https://www.nespresso.com/ecom/medias/sys_master/public/45587298811934/Vanilla-Iced-Matcha-Latte.jpg"
                    ),
                    ProductDto(
                        id = 5,
                        name = "Golden Caramel Toffee Reverso",
                        price = 125.0,
                        imageUrl = "https://www.nespresso.com/ecom/medias/sys_master/public/45579651973150/golden-caramel-toffee-reverso.jpg"
                    ),
                    ProductDto(
                        id = 6,
                        name = "Paris Chocolat Noir",
                        price = 105.0,
                        imageUrl = "https://www.nespresso.com/ecom/medias/sys_master/public/45349018566686/paris-chocolat-noir.jpg"
                    ),
                    ProductDto(
                        id = 7,
                        name = "Caramello Apple Brûlée Cappuccino",
                        price = 130.0,
                        imageUrl = "https://www.nespresso.com/ecom/medias/sys_master/public/45378874376222/caramello-apple-brulee-cappuccino.jpg"
                    ),
                    ProductDto(
                        id = 8,
                        name = "Almond Reverso",
                        price = 110.0,
                        imageUrl = "https://www.nespresso.com/ecom/medias/sys_master/public/45579664130078/almond-reverso.jpg"
                    ),
                    ProductDto(
                        id = 9,
                        name = "Pumpkin Dream Latte",
                        price = 120.0,
                        imageUrl = "https://www.nespresso.com/ecom/medias/sys_master/public/45587301466142/Pumpkin-Dream-Latte.jpg"
                    ),
                    ProductDto(
                        id = 10,
                        name = "Tokyo matcha latte",
                        price = 110.0,
                        imageUrl = "https://www.nespresso.com/ecom/medias/sys_master/public/45380617076766/tokyo-matcha-latte.jpg"
                    )
                )
            )
        )
    )

    val products = listOf(
        Product(
            id = 1,
            name = "Latte",
            price = 95.0,
            imageUrl = "https://www.nespresso.com/ncp/res/uploads/recipes/nespresso-recipes-Latte-Art-Tulip.jpg"
        ),
        Product(
            id = 2,
            name = "Dark Tiramisu Mocha",
            price = 95.0,
            imageUrl = "https://www.nespresso.com/shared_res/mos/free_html/sg/b2b/b2ccoffeerecipes/listing-image/image/dark-tiramisu-mocha.jpg"
        ),
        Product(
            id = 3,
            name = "Tropical Iced Rio",
            price = 110.0,
            imageUrl = "https://www.nespresso.com/ecom/medias/sys_master/public/15476364836894/tropical-iced-rio.jpg"
        ),
        Product(
            id = 4,
            name = "Vanilla Iced Matcha Latte",
            price = 135.0,
            imageUrl = "https://www.nespresso.com/ecom/medias/sys_master/public/45587298811934/Vanilla-Iced-Matcha-Latte.jpg"
        ),
        Product(
            id = 5,
            name = "Golden Caramel Toffee Reverso",
            price = 125.0,
            imageUrl = "https://www.nespresso.com/ecom/medias/sys_master/public/45579651973150/golden-caramel-toffee-reverso.jpg"
        ),
        Product(
            id = 6,
            name = "Paris Chocolat Noir",
            price = 105.0,
            imageUrl = "https://www.nespresso.com/ecom/medias/sys_master/public/45349018566686/paris-chocolat-noir.jpg"
        ),
        Product(
            id = 7,
            name = "Caramello Apple Brûlée Cappuccino",
            price = 130.0,
            imageUrl = "https://www.nespresso.com/ecom/medias/sys_master/public/45378874376222/caramello-apple-brulee-cappuccino.jpg"
        ),
        Product(
            id = 8,
            name = "Almond Reverso",
            price = 110.0,
            imageUrl = "https://www.nespresso.com/ecom/medias/sys_master/public/45579664130078/almond-reverso.jpg"
        ),
        Product(
            id = 9,
            name = "Pumpkin Dream Latte",
            price = 120.0,
            imageUrl = "https://www.nespresso.com/ecom/medias/sys_master/public/45587301466142/Pumpkin-Dream-Latte.jpg"
        ),
        Product(
            id = 10,
            name = "Tokyo matcha latte",
            price = 110.0,
            imageUrl = "https://www.nespresso.com/ecom/medias/sys_master/public/45380617076766/tokyo-matcha-latte.jpg"
        )
    )

}