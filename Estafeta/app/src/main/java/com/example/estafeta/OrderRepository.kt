package com.example.estafeta

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object OrderRepository {

    val orders = mutableStateListOf<Order>()

    // Update the URL of the Firebase Database
    private val database = Firebase.database("https://gogetit-426601-default-rtdb.europe-west1.firebasedatabase.app")
    private val ordersRef = database.getReference("orders")

    fun listenForNewOrders() {
        ordersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                orders.clear()
                for (orderSnapshot in snapshot.children) {
                    val orderData = orderSnapshot.value as HashMap<String, Any>
                    val order = Order(
                        id = orderData["orderId"] as String,
                        restaurantName = orderData["restaurantName"] as String,
                        restaurantLocation = Location(
                            latitude = (orderData["restaurantLatitude"] as Number).toDouble(),
                            longitude = (orderData["restaurantLongitude"] as Number).toDouble()
                        ),
                        clientLocation = Location(
                            latitude = (orderData["clientLatitude"] as Number).toDouble(),
                            longitude = (orderData["clientLongitude"] as Number).toDouble()
                        ),
                        items = (orderData["items"] as List<Map<String, Any>>).map {
                            OrderMenuItem(
                                name = it["name"] as String,
                                price = (it["price"] as Number).toDouble()
                            )
                        },
                        totalPrice = (orderData["totalPrice"] as Number).toDouble(),
                        confirmationCode = orderData["confirmationCode"] as String
                    )
                    orders.add(order)
                    Log.d("OrderRepository", "New order received: $order")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to receive data
                Log.e("OrderRepository", "Failed to receive order", error.toException())
            }
        })
    }
}
