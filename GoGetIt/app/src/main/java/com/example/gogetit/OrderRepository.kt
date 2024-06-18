package com.example.gogetit

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object OrderRepository {
    private const val TAG = "OrderRepository"

    val orders = mutableStateListOf<Order>()


    fun sendOrderToDatabase(
        order: Order,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        Log.d(TAG, "Iniciando envio do pedido para o banco de dados")

        val database = Firebase.database("https://gogetit-426601-default-rtdb.europe-west1.firebasedatabase.app")
        val lastOrderIdRef = database.getReference("lastOrderId")
        val ordersRef = database.getReference("orders")

        lastOrderIdRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lastOrderId = snapshot.getValue(Int::class.java) ?: 0
                val newOrderId = lastOrderId + 1
                order.id = newOrderId.toString()

                val orderData = hashMapOf(
                    "orderId" to order.id,
                    "restaurantName" to order.restaurantName,
                    "restaurantLatitude" to order.restaurantLocation.latitude,
                    "restaurantLongitude" to order.restaurantLocation.longitude,
                    "clientLatitude" to order.clientLocation.latitude,
                    "clientLongitude" to order.clientLocation.longitude,
                    "items" to order.items.map { hashMapOf("name" to it.name, "price" to it.price) },
                    "totalPrice" to order.totalPrice,
                    "confirmationCode" to order.confirmationCode
                )

                ordersRef.child(order.id).setValue(orderData)
                    .addOnSuccessListener {
                        Log.d(TAG, "Pedido adicionado com sucesso à base de dados")
                        lastOrderIdRef.setValue(newOrderId)
                        onSuccess()
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Falha ao adicionar pedido à base de dados", exception)
                        onFailure()
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Falha ao obter o último ID do pedido", error.toException())
                onFailure()
            }
        })
    }

    fun listenForNewOrders() {
        val database = Firebase.database("https://gogetit-426601-default-rtdb.europe-west1.firebasedatabase.app")
        val ordersRef = database.getReference("orders")

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
                // Handle database error
                Log.e("OrderRepository", "Failed to receive order", error.toException())
            }
        })
    }
}

