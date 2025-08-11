package dev.yelinaung.emarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import dagger.hilt.android.AndroidEntryPoint
import dev.yelinaung.emarket.presentation.store.StoreRoute
import dev.yelinaung.emarket.presentation.store.StoreScreen
import dev.yelinaung.emarket.presentation.success.SuccessRoute
import dev.yelinaung.emarket.presentation.success.SuccessScreen
import dev.yelinaung.emarket.presentation.summary.SummaryRoute
import dev.yelinaung.emarket.presentation.summary.SummaryScreen
import dev.yelinaung.emarket.ui.theme.EMarketTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            EMarketTheme {

                val backStack = rememberNavBackStack(StoreRoute)

                NavDisplay(
                    backStack = backStack,
                    onBack = { backStack.removeLastOrNull() },
                    modifier = Modifier.fillMaxSize()
                ) { key ->
                    when (key) {
                        is StoreRoute -> NavEntry(key) {
                            StoreScreen(
                                onClickCheckout = {
                                    backStack.add(SummaryRoute(it))
                                })
                        }

                        is SummaryRoute -> NavEntry(key) {
                            SummaryScreen(
                                products = key.products, onSuccessOrderPlacement = {
                                    backStack.add(SuccessRoute)
                                })
                        }

                        is SuccessRoute -> NavEntry(key) {
                            SuccessScreen(
                                onClickButton = {
                                    backStack.clear()
                                    backStack.add(StoreRoute)
                                })
                        }

                        else -> {
                            error("Unknown route: $key")
                        }
                    }
                }

            }
        }
    }

}