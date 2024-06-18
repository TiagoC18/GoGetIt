package com.example.gogetit

import androidx.annotation.DrawableRes

data class Restaurant(
    val name: String,
    @DrawableRes val image: Int,
    val rating: Float,
    val deliveryTime: String,
    val latitude: Double,
    val longitude: Double,
    val menu: List<MenuItem>
)


data class MenuItem(
    val name: String,
    val price: String,
    @DrawableRes val image: Int
)
