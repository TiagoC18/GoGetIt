package com.example.estafeta

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickupMapScreen(navController: NavController, orderId: String?, latitude: Double?, longitude: Double?) {
    val order = remember { OrderRepository.orders.find { it.id == orderId } }
    val clientLatitude = order?.clientLocation?.latitude ?: 0.0
    val clientLongitude = order?.clientLocation?.longitude ?: 0.0
    val initialLocation = LatLng(latitude ?: 0.0, longitude ?: 0.0)
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(initialLocation, 15f)
    }
    val markerState = rememberMarkerState(position = initialLocation)

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Pickup Directions") },
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
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState
                    ) {
                        Marker(
                            state = markerState,
                            title = "Restaurant Location"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        navController.navigate("deliveryMap/$orderId/$clientLatitude/$clientLongitude")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Comida recebida")
                }
            }
        }
    )
}
