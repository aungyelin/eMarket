package dev.yelinaung.emarket.network

import dev.yelinaung.emarket.network.exception.NetworkException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class SafeApiCallTest {

    @Test
    fun `safeApiCall when apiCall is successful should return Result success`() = runTest {

        val expectedData = "Success Data"

        val result = safeApiCall {
            expectedData
        }

        assertTrue(result.isSuccess)
        assertEquals(expectedData, result.getOrNull())

    }

    @Test
    fun `safeApiCall when apiCall throws HttpException 404 should return Result failure with NetworkException`() = runTest {

        val errorMessage = "Test 404 Error"
        val response = Response.error<Any>(404, errorMessage.toResponseBody("application/json".toMediaTypeOrNull()))
        val httpException = HttpException(response)

        val result = safeApiCall<Any> {
            throw httpException
        }

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NetworkException)
        assertEquals("Not Found", (exception as NetworkException).message)
        assertEquals(404, exception.code)

    }

    @Test
    fun `safeApiCall when apiCall throws HttpException 429 should return Result failure with NetworkException`() = runTest {

        val errorMessage = "Test 429 Error"
        val response = Response.error<Any>(429, errorMessage.toResponseBody("application/json".toMediaTypeOrNull()))
        val httpException = HttpException(response)

        val result = safeApiCall<Any> {
            throw httpException
        }

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NetworkException)
        assertEquals("Usage limit reached", (exception as NetworkException).message)
        assertEquals(429, exception.code)

    }

    @Test
    fun `safeApiCall when apiCall throws HttpException 500 should return Result failure with NetworkException`() = runTest {

        val errorMessage = "Test 500 Error"
        val response = Response.error<Any>(500, errorMessage.toResponseBody("application/json".toMediaTypeOrNull()))
        val httpException = HttpException(response)

        val result = safeApiCall<Any> {
            throw httpException
        }

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NetworkException)
        assertEquals("Internal Server Error", (exception as NetworkException).message)
        assertEquals(500, exception.code)

    }

    @Test
    fun `safeApiCall when apiCall throws HttpException with other code should return Result failure with NetworkException`() = runTest {

        val errorMessage = "Test 403 Error"
        val response = Response.error<Any>(403, errorMessage.toResponseBody("application/json".toMediaTypeOrNull()))
        val httpException = HttpException(response)

        val result = safeApiCall<Any> {
            throw httpException
        }

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NetworkException)
        assertEquals(403, (exception as NetworkException).code)

    }

    @Test
    fun `safeApiCall when apiCall throws IOException should return Result failure with NetworkException`() = runTest {

        val ioException = IOException("Network connection failed")

        val result = safeApiCall<Any> {
            throw ioException
        }

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NetworkException)
        assertEquals("Network error", (exception as NetworkException).message)
        assertEquals(-1, exception.code)

    }

    @Test
    fun `safeApiCall when apiCall throws other Throwable should return Result failure with original throwable`() = runTest {

        val genericException = RuntimeException("Some generic error")

        val result = safeApiCall<Any> {
            throw genericException
        }

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is RuntimeException)
        assertEquals(genericException, exception)

    }

}
