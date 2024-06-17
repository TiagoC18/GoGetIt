package com.example.estafeta

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController)
        }
        composable("signup") {
            SignUpScreen(navController)
        }
        composable("main") {
            MainScreen(navController)
        }
        composable("orderDetail/{orderId}") { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId")
            if (orderId != null) {
                OrderDetailScreen(navController, orderId)
            }
        }
        composable("pickupMap/{orderId}") { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId")
            PickupMapScreen(navController, orderId)
        }
        composable("deliveryMap/{orderId}") { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId")
            DeliveryMapScreen(navController, orderId)
        }
    }
}
