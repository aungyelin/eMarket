package dev.yelinaung.emarket.network

import dev.yelinaung.emarket.network.exception.NetworkException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    apiCall: suspend () -> T,
): Result<T> {
    return withContext(Dispatchers.IO) {
        try {
            Result.success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> {
                    val message = when (throwable.code()) {
                        404 -> "Not Found"
                        429 -> "Usage limit reached"
                        500 -> "Internal Server Error"
                        else -> throwable.message ?: "Something went wrong"
                    }
                    Result.failure(NetworkException(message, throwable.code()))
                }

                is IOException -> {
                    Result.failure(NetworkException("Network error"))
                }

                else -> {
                    Result.failure(throwable)
                }
            }
        }
    }
}
