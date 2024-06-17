package com.example.gogetit

import android.util.Log
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object OrderRepository {
    private const val TAG = "OrderRepository"

    fun sendOrderToDatabase(
        order: Order,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        Log.d(TAG, "Iniciando envio do pedido para o banco de dados")

        val database = Firebase.database
        val orderRef = database.getReference("orders").push()

        val orderData = hashMapOf(
            "orderId" to order.id,
            "restaurantName" to order.restaurantName,
            "restaurantLatitude" to order.restaurantLocation.latitude,
            "restaurantLongitude" to order.restaurantLocation.longitude,
            "clientLatitude" to order.clientLocation.latitude,
            "clientLongitude" to order.clientLocation.longitude,
            "items" to order.items.map { it.name to it.price }.toMap(),
            "totalPrice" to order.totalPrice
        )

        orderRef.setValue(orderData)
            .addOnSuccessListener {
                Log.d(TAG, "Pedido adicionado com sucesso à base de dados")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Falha ao adicionar pedido à base de dados", exception)
                onFailure()
            }
    }
}
