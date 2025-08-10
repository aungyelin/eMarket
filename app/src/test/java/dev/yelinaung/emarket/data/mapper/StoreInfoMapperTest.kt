package dev.yelinaung.emarket.data.mapper

import dev.yelinaung.emarket.network.dto.StoreInfoDto
import org.junit.Assert.assertEquals
import org.junit.Test

class StoreInfoMapperTest {

    @Test
    fun `map StoreInfoDto to StoreInfo`() {

        val storeInfoDto = StoreInfoDto(
            name = "eMarket",
            rating = 4.5,
            openingTime = "15:01:01.772Z",
            closingTime = "19:45:51.365Z"
        )

        val storeInfo = storeInfoDto.toDomain()

        assertEquals("eMarket", storeInfo.name)
        assertEquals(4.5, storeInfo.rating, 0.0)
        assertEquals("15:01:01.772Z", storeInfo.openingTime)
        assertEquals("19:45:51.365Z", storeInfo.closingTime)

    }
}
