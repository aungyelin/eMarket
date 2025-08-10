package dev.yelinaung.emarket.domain.model

sealed class DataException(override val message: String) : Exception(message) {
    data class Network(override val message: String, val code: Int = -1) : DataException(message)
    data class Other(override val message: String) : DataException(message)
}
