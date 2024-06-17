package com.example.estafeta

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickupMapScreen(navController: NavController, orderId: String?) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Pickup Directions") })
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Placeholder for Map
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.navigate("deliveryMap/$orderId") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Comida recebida")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { /* Chat functionality to be implemented */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Chat")
                }
            }
        }
    )
}
