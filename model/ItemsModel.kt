package com.example.kampus2.model

import java.io.Serializable

data class ItemsModel(
    var id: Int=0,
    var title: String="",
    var category: String="",
    var quantity: Int=0,
    var price: Double=0.0,
    var picUrl: String ="",
    var numberInCart: Int=1
): Serializable
