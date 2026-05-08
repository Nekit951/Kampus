package com.example.kampus2.model

import java.io.Serializable

data class OrderModel(
    val items: ArrayList<ItemsModel>,
    val summa: Double = 0.0,
    val address: String = "",
    val payMethod: String = ""
): Serializable
