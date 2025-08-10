package dev.yelinaung.emarket.data.mapper

import dev.yelinaung.emarket.domain.model.DataException
import dev.yelinaung.emarket.network.exception.NetworkException

fun <T> Result<T>.mapNetworkErrorToDataException(): Result<T> {
    return this.fold(onSuccess = { value -> Result.success(value) }, onFailure = { throwable ->
        when (throwable) {
            is NetworkException -> {
                val dataException = DataException.Network(throwable.message, throwable.code)
                Result.failure(dataException)
            }

            else -> {
                Result.failure(throwable)
            }
        }
    })
}
