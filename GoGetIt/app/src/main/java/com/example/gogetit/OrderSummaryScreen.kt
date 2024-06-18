package com.example.gogetit

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderSummaryScreen(
    navController: NavController,
    selectedRestaurant: Restaurant?,
    cartItems: MutableMap<MenuItem, Int>,
    onPlaceOrder: (String, String) -> Unit
) {
    var paymentMethod by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showFailureDialog by remember { mutableStateOf(false) }
    var userLocation by remember { mutableStateOf<com.example.gogetit.Location?>(null) }

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
            RequestLocationPermissions(
                onPermissionsGranted = {
                    val context = LocalContext.current
                    LaunchedEffect(Unit) {
                        val location = getCurrentLocation(context)
                        userLocation = location?.toCustomLocation()
                    }
                },
                onPermissionsDenied = {
                    // Handle the case where permissions are denied (future improvement)
                }
            )

            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                item {
                    Text(
                        text = "Order Summary",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

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

                item {
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

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
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
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = TextFieldDefaults.outlinedTextFieldColors(
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
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
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = TextFieldDefaults.outlinedTextFieldColors(
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val confirmationCode = (1000..9999).random().toString()
                            val order = Order(
                                id = "",
                                restaurantName = selectedRestaurant?.name ?: "Unknown",
                                restaurantLocation = Location(selectedRestaurant?.latitude ?: 0.0, selectedRestaurant?.longitude ?: 0.0),
                                clientLocation = userLocation ?: Location(0.0, 0.0),
                                items = cartItems.entries.map { OrderMenuItem(it.key.name, it.key.price.removePrefix("$").toDouble()) },
                                totalPrice = totalCost,
                                confirmationCode = confirmationCode
                            )
                            Log.d("OrderSummaryScreen", "Tentando enviar pedido para a base de dados")
                            OrderRepository.sendOrderToDatabase(order,
                                onSuccess = {
                                    Log.d("OrderSummaryScreen", "Pedido enviado com sucesso")
                                    onPlaceOrder(order.id, order.confirmationCode)
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
        }
    )

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
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = { onClick() }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}
