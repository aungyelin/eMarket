package dev.yelinaung.emarket.presentation.summary

sealed class SummaryUiState {
    object Idle : SummaryUiState()
    object Loading : SummaryUiState()
    object Success : SummaryUiState()
    data class Error(val message: String) : SummaryUiState()
}