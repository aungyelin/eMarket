package dev.yelinaung.emarket.network.exception

data class NetworkException(
    override val message: String,
    val code: Int = -1
) : Exception(message)
