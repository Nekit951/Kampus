package com.example.kampus2.helpers

import android.content.Context
import com.example.kampus2.model.OrderModel

class ManagmentOrder(val context: Context) {
    private val tinyDB = TinyDB(context)

    fun insertOrder(order: OrderModel) {
        var orders = getListOrder()
        orders.add(0, order)
        tinyDB.putListOrder("OrdersHistory", orders as ArrayList<OrderModel?>)
    }

    fun getListOrder(): ArrayList<OrderModel> {
        return tinyDB.getListOrder("OrdersHistory") as ArrayList<OrderModel>
    }

    fun removeOrderAt(position: Int) {
        val orders = getListOrder()
        if (position >= 0 && position < orders.size) {
            orders.removeAt(position)
            tinyDB.putListOrder("OrdersHistory", orders as ArrayList<OrderModel?>)
        }
    }
}