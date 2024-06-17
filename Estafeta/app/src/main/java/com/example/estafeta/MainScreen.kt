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

data class Order(val id: String, val description: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    val orders = remember {
        listOf(
            Order(id = "1", description = "Order 1: Pizza from Restaurant A"),
            Order(id = "2", description = "Order 2: Burger from Restaurant B")
        )
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
                            Text(text = order.description, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    )
}
