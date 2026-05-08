package com.example.kampus2.model

import java.io.Serializable

data class Card(
    val number: String = "",
    val data: String = "",
    val cvp: String = ""
): Serializable
