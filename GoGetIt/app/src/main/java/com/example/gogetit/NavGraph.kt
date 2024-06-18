package com.example.gogetit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun NavGraph(navController: NavHostController) {
    val restaurants = listOf(
        Restaurant(
            name = "Rose Garden Restaurant",
            image = R.drawable.rose_garden,
            rating = 4.7f,
            deliveryTime = "20 min",
            latitude = 40.64634189966905,
            longitude = -8.62830780581242,
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
            latitude = 40.627214659115246,
            longitude = -8.651221847871629,
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
            latitude = 40.64025655391656,
            longitude = -8.64366021399209,
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
            latitude = 40.648080467979035,
            longitude = -8.644118494833275,
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
            latitude = 40.63747449678834,
            longitude = -8.632432333383079,
            menu = listOf(
                MenuItem("Quinoa Salad", "$9", R.drawable.quinoa_salad),
                MenuItem("Avocado Toast", "$7", R.drawable.avocado_toast),
                MenuItem("Smoothie Bowl", "$8", R.drawable.smoothie_bowl)
            )
        )
    )

    val cartItems = remember { mutableStateMapOf<MenuItem, Int>() }
    var selectedRestaurant by remember { mutableStateOf<Restaurant?>(null) }

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
            selectedRestaurant = restaurants.find { it.name == restaurantName }
            selectedRestaurant?.let { restaurant ->
                RestaurantDetailScreen(navController, restaurant) { menuItem ->
                    cartItems[menuItem] = cartItems.getOrDefault(menuItem, 0) + 1
                }
            }
        }
        composable("cart") {
            CartScreen(navController, cartItems) {
                navController.navigate("orderSummary/${selectedRestaurant?.name}")
            }
        }
        composable("orderSummary/{restaurantName}") { backStackEntry ->
            val restaurantName = backStackEntry.arguments?.getString("restaurantName")
            selectedRestaurant = restaurants.find { it.name == restaurantName }
            OrderSummaryScreen(navController, selectedRestaurant, cartItems) { orderId, confirmationCode ->
                navController.navigate("orderConfirmation/$orderId/$confirmationCode")
            }
        }
        composable(
            "orderConfirmation/{orderId}/{confirmationCode}",
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType },
                navArgument("confirmationCode") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId")
            val confirmationCode = backStackEntry.arguments?.getString("confirmationCode")
            OrderConfirmationScreen(navController, orderId, confirmationCode)
        }
        composable("orderHistory") {
            OrderHistoryScreen(navController)
        }
        composable("orderDetail/{orderId}", arguments = listOf(
            navArgument("orderId") { type = NavType.StringType }
        )) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId")
            OrderDetailScreen(navController, orderId)
        }
    }
}
