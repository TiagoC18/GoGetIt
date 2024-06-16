package com.example.gogetit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun NavGraph(navController: NavHostController) {
    val restaurants = listOf(
        Restaurant(
            name = "Rose Garden Restaurant",
            image = R.drawable.rose_garden,
            rating = 4.7f,
            deliveryTime = "20 min",
            menu = listOf(
                MenuItem("Pasta Carbonara", "$12", R.drawable.pasta_carbonara),
                MenuItem("Margherita Pizza", "$15", R.drawable.margherita_pizza),
                MenuItem("Tiramisu", "$8", R.drawable.tiramisu)
            )
        ),
        Restaurant(
            name = "Spicy Delights",
            image = R.drawable.spicy_delights,
            rating = 4.5f,
            deliveryTime = "15 min",
            menu = listOf(
                MenuItem("Spicy Chicken Wings", "$10", R.drawable.spicy_chicken_wings),
                MenuItem("Jalapeno Poppers", "$7", R.drawable.jalapeno_poppers),
                MenuItem("Spicy Ramen", "$12", R.drawable.spicy_ramen)
            )
        ),
        Restaurant(
            name = "Sushi World",
            image = R.drawable.sushi_world,
            rating = 4.8f,
            deliveryTime = "25 min",
            menu = listOf(
                MenuItem("California Roll", "$12", R.drawable.california_roll),
                MenuItem("Spicy Tuna Roll", "$14", R.drawable.spicy_tuna_roll),
                MenuItem("Miso Soup", "$5", R.drawable.miso_soup)
            )
        ),
        Restaurant(
            name = "Burger Kingdom",
            image = R.drawable.burger_kingdom,
            rating = 4.3f,
            deliveryTime = "20 min",
            menu = listOf(
                MenuItem("Cheeseburger", "$10", R.drawable.cheeseburger),
                MenuItem("Bacon Burger", "$12", R.drawable.bacon_burger),
                MenuItem("Fries", "$5", R.drawable.fries)
            )
        ),
        Restaurant(
            name = "Healthy Bites",
            image = R.drawable.healthy_bites,
            rating = 4.6f,
            deliveryTime = "18 min",
            menu = listOf(
                MenuItem("Quinoa Salad", "$9", R.drawable.quinoa_salad),
                MenuItem("Avocado Toast", "$7", R.drawable.avocado_toast),
                MenuItem("Smoothie Bowl", "$8", R.drawable.smoothie_bowl)
            )
        )
    )

    val cartItems = remember { mutableStateMapOf<MenuItem, Int>() }

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
                RestaurantDetailScreen(navController, restaurant) { menuItem ->
                    cartItems[menuItem] = cartItems.getOrDefault(menuItem, 0) + 1
                }
            }
        }
        composable("cart") {
            CartScreen(navController, cartItems) {
                navController.navigate("orderSummary")
            }
        }
        composable("profile") {
            ProfileScreen(navController)
        }
        composable("orderSummary") {
            OrderSummaryScreen(navController, cartItems) {
                navController.navigate("orderConfirmation")
            }
        }
        composable("orderConfirmation") {
            OrderConfirmationScreen(navController)
        }
    }
}