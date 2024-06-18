package com.example.estafeta

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    val orders by remember { derivedStateOf { OrderRepository.orders } }

    // Start listening for new orders
    LaunchedEffect(Unit) {
        OrderRepository.listenForNewOrders()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Orders") },
                actions = {
                    IconButton(onClick = { navController.navigate("login") }) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Log Out")
                    }
                }
            )
        },
        content = { paddingValues ->
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                items(orders) { order ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                navController.navigate("orderDetail/${order.id}/${order.restaurantLocation.latitude}/${order.restaurantLocation.longitude}")
                            }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Order ${order.id}: ${order.restaurantName}", style = MaterialTheme.typography.bodyLarge)
                            Text(text = "Total Price: $${order.totalPrice}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    )
}
