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

    fun listenForNewOrders() {
        val database = Firebase.database
        val ordersRef = database.getReference("orders")

        ordersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                orders.clear()
                for (orderSnapshot in snapshot.children) {
                    val order = orderSnapshot.getValue(Order::class.java)
                    if (order != null) {
                        orders.add(order)
                        Log.d("OrderRepository", "Novo pedido recebido: $order")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Falha ao receber dados
                Log.e("OrderRepository", "Erro ao receber o pedido", error.toException())
            }
        })
    }
}
