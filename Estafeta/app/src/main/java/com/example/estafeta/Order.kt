package com.example.estafeta

data class Order(
    var id: String = "",
    val restaurantName: String = "",
    val restaurantLocation: Location = Location(),
    val clientLocation: Location = Location(),
    val items: List<OrderMenuItem> = emptyList(),
    val totalPrice: Double = 0.0,
    val confirmationCode: String = ""
)

data class Location(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

data class OrderMenuItem(
    val name: String = "",
    val price: Double = 0.0
)
