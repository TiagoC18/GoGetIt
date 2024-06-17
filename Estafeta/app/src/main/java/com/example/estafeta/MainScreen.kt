package com.example.estafeta

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    // Obter a lista de pedidos do OrderRepository
    val orders by remember { derivedStateOf { OrderRepository.orders } }

    // Iniciar a escuta de novos pedidos
    LaunchedEffect(Unit) {
        OrderRepository.listenForNewOrders()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Orders") })
        },
        content = { paddingValues ->
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                items(orders) { order ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                navController.navigate("orderDetail/${order.id}")
                            }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Order ${order.id}: ${order.restaurantName}", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    )
}
