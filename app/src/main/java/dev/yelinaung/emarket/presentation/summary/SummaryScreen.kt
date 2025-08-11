package dev.yelinaung.emarket.presentation.summary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size // Added import
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import dev.yelinaung.emarket.domain.model.Product
import dev.yelinaung.emarket.presentation.store.ProductUiModel
import kotlinx.serialization.Serializable
import java.util.Locale
import java.util.UUID

@Serializable
data class SummaryRoute(val products: List<ProductUiModel>) : NavKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(
    products: List<ProductUiModel>,
    onSuccessOrderPlacement: () -> Unit,
    viewModel: SummaryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var address by remember { mutableStateOf("") }
    val isAddressValid = address.isNotBlank()
    val totalAmount = products.sumOf { it.product.price * it.quantity }

    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    when (uiState) {
        SummaryUiState.Loading -> {
            // Show loading indicator in the button
        }
        SummaryUiState.Success -> {
            onSuccessOrderPlacement()
            viewModel.resetUiState()
        }
        is SummaryUiState.Error -> {
            dialogMessage = (uiState as SummaryUiState.Error).message
            showDialog = true
            viewModel.resetUiState()
        }
        SummaryUiState.Idle -> {
            // Do nothing
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Error") },
            text = { Text(dialogMessage) },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Order Summary") })
        },
        bottomBar = {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Total", style = MaterialTheme.typography.headlineSmall)
                        val formattedPrice = if (totalAmount % 1 == 0.0) {
                            String.format(Locale.US, "%,.0f", totalAmount)
                        } else {
                            String.format(Locale.US, "%,.2f", totalAmount)
                        }
                        Text(
                            text = "$$formattedPrice",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.order(products, address) },
                        enabled = isAddressValid && uiState != SummaryUiState.Loading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (uiState == SummaryUiState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text("Place Order")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Delivery Address") },
                modifier = Modifier.fillMaxWidth(),
                isError = !isAddressValid && address.isNotEmpty(),
                enabled = uiState != SummaryUiState.Loading
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(products) { product ->
                    ProductSummaryItem(product)
                }
            }
        }
    }
}

@Composable
fun ProductSummaryItem(product: ProductUiModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = product.product.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Qty: ${product.quantity}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        val totalPrice = product.product.price * product.quantity
        val formattedPrice = if (totalPrice % 1 == 0.0) {
            String.format(Locale.US, "%,.0f", totalPrice)
        } else {
            String.format(Locale.US, "%,.2f", totalPrice)
        }
        Text(text = "$$formattedPrice", style = MaterialTheme.typography.titleMedium)
    }
}

@Preview
@Composable
fun SummaryScreenPreview() {
    SummaryScreen(
        products = listOf(
            ProductUiModel(
                uuid = UUID.randomUUID(),
                product = Product(
                    name = "Product 1",
                    price = 10.0,
                    imageUrl = ""
                ),
                quantity = 2,
                isSelected = true
            ),
            ProductUiModel(
                uuid = UUID.randomUUID(),
                product = Product(
                    name = "Product 2",
                    price = 15.50,
                    imageUrl = ""
                ),
                quantity = 1,
                isSelected = true
            )
        ),
        onSuccessOrderPlacement = {}
    )
}
