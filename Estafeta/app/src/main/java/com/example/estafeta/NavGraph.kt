package com.example.estafeta

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument

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
        composable(
            "orderDetail/{orderId}/{latitude}/{longitude}",
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType },
                navArgument("latitude") { type = NavType.FloatType },
                navArgument("longitude") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId")
            val latitude = backStackEntry.arguments?.getFloat("latitude")?.toDouble()
            val longitude = backStackEntry.arguments?.getFloat("longitude")?.toDouble()
            OrderDetailScreen(navController, orderId, latitude, longitude)
        }
        composable(
            "pickupMap/{orderId}/{latitude}/{longitude}",
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType },
                navArgument("latitude") { type = NavType.FloatType },
                navArgument("longitude") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId")
            val latitude = backStackEntry.arguments?.getFloat("latitude")?.toDouble()
            val longitude = backStackEntry.arguments?.getFloat("longitude")?.toDouble()
            PickupMapScreen(navController, orderId, latitude, longitude)
        }
        composable(
            "deliveryMap/{orderId}/{latitude}/{longitude}",
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType },
                navArgument("latitude") { type = NavType.FloatType },
                navArgument("longitude") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId")
            val latitude = backStackEntry.arguments?.getFloat("latitude")?.toDouble()
            val longitude = backStackEntry.arguments?.getFloat("longitude")?.toDouble()
            DeliveryMapScreen(navController, orderId, latitude, longitude)
        }
    }
}
