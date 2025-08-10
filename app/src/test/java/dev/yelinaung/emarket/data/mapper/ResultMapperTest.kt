package dev.yelinaung.emarket.data.mapper

import dev.yelinaung.emarket.domain.model.DataException
import dev.yelinaung.emarket.network.exception.NetworkException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ResultMapperTest {

    @Test
    fun `map success result`() {

        val input = Result.success("success")

        val result = input.mapNetworkErrorToDataException()

        assertTrue(result.isSuccess)
        assertFalse(result.isFailure)
        assertEquals(input, result)
        assertEquals("success", result.getOrNull())

    }

    @Test
    fun `map failure result with Exception`() {

        val exception = Exception("failure")
        val input = Result.failure<String>(exception)

        val result = input.mapNetworkErrorToDataException()

        assertTrue(result.isFailure)
        assertFalse(result.isSuccess)
        assertTrue(result.exceptionOrNull() is Throwable)
        assertFalse(result.exceptionOrNull() is DataException)
        assertEquals(exception, result.exceptionOrNull())
        assertEquals("failure", result.exceptionOrNull()?.message)

    }

    @Test
    fun `map failure result with NetworkException`() {

        val exception = NetworkException("Network Error", 404)
        val input = Result.failure<String>(exception)

        val result = input.mapNetworkErrorToDataException()

        assertTrue(result.isFailure)
        assertFalse(result.isSuccess)
        assertTrue(result.exceptionOrNull() is Throwable)
        assertTrue(result.exceptionOrNull() is DataException)
        assertTrue(result.exceptionOrNull() is DataException.Network)
        assertFalse(result.exceptionOrNull() is DataException.Other)
        assertFalse(result.exceptionOrNull() == exception)
        assertEquals("Network Error", result.exceptionOrNull()?.message)
        assertEquals("Network Error", (result.exceptionOrNull() as DataException.Network).message)
        assertEquals(404, (result.exceptionOrNull() as DataException.Network).code)

    }

}
