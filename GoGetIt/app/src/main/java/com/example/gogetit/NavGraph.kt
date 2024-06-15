package com.example.gogetit

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraph(navController: NavHostController) {
    val restaurants = listOf(
        Restaurant("Rose Garden Restaurant", R.drawable.restaurant_image, 4.7f, "20 min"),
        Restaurant("Spicy Restaurant", R.drawable.restaurant_image, 4.5f, "15 min")
    )

    val menuItems = listOf(
        MenuItem("Pizza Calzone European", "$32", R.drawable.pizza_image),
        MenuItem("Burger Ferguson", "$40", R.drawable.burger_image)
    )

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController)
        }
        composable("signup") {
            SignUpScreen(navController)
        }
        composable("main") {
            MainScreen(navController, restaurants)
        }
        composable("restaurantDetail/{restaurantName}") { backStackEntry ->
            val restaurantName = backStackEntry.arguments?.getString("restaurantName")
            val selectedRestaurant = restaurants.find { it.name == restaurantName }
            selectedRestaurant?.let { restaurant ->
                RestaurantDetailScreen(navController, restaurant, menuItems)
            }
        }
        composable("cart") {
            CartScreen(navController, menuItems)
        }
        composable("profile") {
            ProfileScreen(navController)
        }
    }
}
