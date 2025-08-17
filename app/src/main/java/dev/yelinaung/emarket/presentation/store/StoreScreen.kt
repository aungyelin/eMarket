package dev.yelinaung.emarket.presentation.store

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.NavKey
import coil.compose.AsyncImage
import dev.yelinaung.emarket.domain.model.Product
import dev.yelinaung.emarket.domain.model.StoreInfo
import kotlinx.serialization.Serializable
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Serializable
data object StoreRoute : NavKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreScreen(
    onClickCheckout: (products: List<ProductUiModel>) -> Unit,
) {
    val viewModel: StoreViewModel = hiltViewModel()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

        val uiState by viewModel.uiState.collectAsState()
        val selectedProducts = uiState.products.filter { it.isSelected }
        val totalQuantity = selectedProducts.sumOf { it.quantity }

        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = viewModel::getStoreData,
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            if (uiState.error != null) {
                ErrorState(
                    errorMessage = uiState.error?.message ?: "Unknown error",
                    onRetry = viewModel::getStoreData
                )
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    uiState.storeInfo?.let {
                        StoreInfoCard(storeInfo = it)
                    }
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(uiState.products) { product ->
                            ProductItem(
                                uiProduct = product,
                                onProductSelected = viewModel::onProductSelected,
                                onQuantityChanged = viewModel::onQuantityChanged
                            )
                        }
                    }
                    if (selectedProducts.isNotEmpty()) {
                        CheckoutButton(
                            totalPrice = selectedProducts.sumOf { it.product.price * it.quantity },
                            onCheckout = {
                                onClickCheckout(selectedProducts.filter { it.quantity > 0 })
                            },
                            enabled = totalQuantity > 0
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun ErrorState(errorMessage: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = errorMessage, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}

@Composable
fun StoreInfoCard(storeInfo: StoreInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = "https://picsum.photos/seed/${storeInfo.name}/800/400",
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomStart)
            ) {
                Text(
                    text = storeInfo.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color.Yellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = " ${storeInfo.rating}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Hours",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = " ${formatTime(storeInfo.openingTime)} - ${formatTime(storeInfo.closingTime)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }
            }
        }
    }
}

private fun formatTime(timeString: String): String {
    return try {
        val time =
            LocalTime.parse(timeString.substringBefore('.'), DateTimeFormatter.ISO_LOCAL_TIME)
        time.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.US))
    } catch (_: Exception) {
        timeString
    }
}

@Composable
fun ProductItem(
    uiProduct: ProductUiModel,
    onProductSelected: (ProductUiModel) -> Unit,
    onQuantityChanged: (ProductUiModel, Int) -> Unit,
) {
    val borderColor = if (uiProduct.isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Transparent
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onProductSelected(uiProduct) }
            .border(2.dp, borderColor, RoundedCornerShape(8.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 9.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box {
                AsyncImage(
                    model = uiProduct.product.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                if (uiProduct.isSelected) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .align(Alignment.Center)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = Color.White,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, end = 0.dp, top = 5.dp, bottom = 5.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text(text = uiProduct.product.name, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$${uiProduct.product.price}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    if (uiProduct.isSelected) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                modifier = Modifier.size(16.dp),
                                onClick = {
                                    onQuantityChanged(
                                        uiProduct,
                                        uiProduct.quantity - 1
                                    )
                                }) {
                                Icon(
                                    modifier = Modifier.size(16.dp),
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Remove"
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(text = uiProduct.quantity.toString())
                            Spacer(modifier = Modifier.width(14.dp))
                            IconButton(
                                modifier = Modifier.size(16.dp),
                                onClick = {
                                    onQuantityChanged(
                                        uiProduct,
                                        uiProduct.quantity + 1
                                    )
                                }) {
                                Icon(
                                    modifier = Modifier.size(16.dp),
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add"
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ProductItemPreview() {
    ProductItem(
        uiProduct = ProductUiModel(
            product = Product(
                id = 1,
                name = "Product Name",
                price = 10.0,
                imageUrl = "https://picsum.photos/200"
            ), isSelected = true
        ),
        onProductSelected = {},
        onQuantityChanged = { _, _ -> }
    )
}

@Composable
fun CheckoutButton(totalPrice: Double, onCheckout: () -> Unit, enabled: Boolean) {
    Button(
        onClick = onCheckout, modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        enabled = enabled
    ) {
        val formattedPrice = if (totalPrice % 1 == 0.0) {
            String.format(Locale.US, "%,.0f", totalPrice)
        } else {
            String.format(Locale.US, "%,.2f", totalPrice)
        }
        Text(text = "Checkout ($$formattedPrice)")
    }
}
