package com.example.gogetit

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderSummaryScreen(
    navController: NavController,
    cartItems: MutableMap<MenuItem, Int>,
    onPlaceOrder: () -> Unit
) {
    var paymentMethod by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showFailureDialog by remember { mutableStateOf(false) }

    val totalCost = cartItems.entries.sumOf { it.key.price.removePrefix("$").toDouble() * it.value }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Summary") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("login") }) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Log Out")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
                Text(
                    text = "Order Summary",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn {
                    items(cartItems.entries.toList()) { entry ->
                        CartItemCard(
                            menuItem = entry.key,
                            quantity = entry.value,
                            onIncreaseQuantity = {
                                cartItems[entry.key] = cartItems.getOrDefault(entry.key, 0) + 1
                            },
                            onDecreaseQuantity = {
                                val currentQuantity = cartItems[entry.key]!!
                                if (currentQuantity > 1) {
                                    cartItems[entry.key] = currentQuantity - 1
                                } else {
                                    cartItems.remove(entry.key)
                                }
                            },
                            onRemoveItem = {
                                cartItems.remove(entry.key)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Total Cost: $${String.format("%.2f", totalCost)}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Select Payment Method",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Column {
                    RadioButtonWithLabel(
                        selected = paymentMethod == "Card",
                        onClick = { paymentMethod = "Card" },
                        label = "Credit/Debit Card"
                    )

                    if (paymentMethod == "Card") {
                        OutlinedTextField(
                            value = cardNumber,
                            onValueChange = { cardNumber = it },
                            label = { Text("Card Number") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    RadioButtonWithLabel(
                        selected = paymentMethod == "PayPal",
                        onClick = { paymentMethod = "PayPal" },
                        label = "PayPal"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    RadioButtonWithLabel(
                        selected = paymentMethod == "MBWay",
                        onClick = { paymentMethod = "MBWay" },
                        label = "MBWay"
                    )

                    if (paymentMethod == "MBWay") {
                        OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = { Text("Phone Number") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val order = Order(
                            id = "some_unique_id",
                            restaurantName = "Restaurant Name",
                            restaurantLocation = Location(40.7128, -74.0060), // Exemplo de coordenadas
                            clientLocation = Location(40.730610, -73.935242), // Exemplo de coordenadas
                            items = cartItems.entries.map { OrderMenuItem(it.key.name, it.key.price.removePrefix("$").toDouble()) },
                            totalPrice = totalCost
                        )
                        Log.d("OrderSummaryScreen", "Tentando enviar pedido para a base de dados")
                        OrderRepository.sendOrderToDatabase(order,
                            onSuccess = {
                                Log.d("OrderSummaryScreen", "Pedido enviado com sucesso")
                                showSuccessDialog = true
                            },
                            onFailure = {
                                Log.e("OrderSummaryScreen", "Falha ao enviar o pedido")
                                showFailureDialog = true
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(text = "Place Order")
                }
            }
        }
    )

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Success") },
            text = { Text("Order placed successfully!") },
            confirmButton = {
                Button(onClick = {
                    showSuccessDialog = false
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = true }
                    }
                }) {
                    Text("OK")
                }
            }
        )
    }

    if (showFailureDialog) {
        AlertDialog(
            onDismissRequest = { showFailureDialog = false },
            title = { Text("Failure") },
            text = { Text("Failed to place the order. Please try again.") },
            confirmButton = {
                Button(onClick = {
                    showFailureDialog = false
                }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun RadioButtonWithLabel(selected: Boolean, onClick: () -> Unit, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}