package com.example.kampus2.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kampus2.adapters.OrderDetailAdapter
import com.example.kampus2.databinding.ActivityOrderDetailBinding
import com.example.kampus2.helpers.ManagmentCart
import com.example.kampus2.helpers.ManagmentOrder
import com.example.kampus2.model.ItemsModel
import com.google.firebase.database.FirebaseDatabase

class OrderDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderDetailBinding
    private lateinit var managmentCart: ManagmentCart
    private lateinit var managmentOrder: ManagmentOrder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        managmentCart = ManagmentCart(this)
        managmentOrder = ManagmentOrder(this)

        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        initOrderDetail()
        cancell()
    }

    private fun initOrderDetail() {
        val orderedItems = intent.getSerializableExtra("ITEMS_LIST") as? ArrayList<ItemsModel> ?: arrayListOf()
        val totalPrice = intent.getDoubleExtra("FINAL_PRICE", 0.0)
        val userAddress = intent.getStringExtra("USER_ADDRESS") ?: "Адрес не указан"
        val payMethod = intent.getStringExtra("PAY_METHOD") ?: "Способ не указан"

        binding.apply {
            recyclerViewOrderDetail.layoutManager = LinearLayoutManager(
                this@OrderDetailActivity,
                LinearLayoutManager.VERTICAL, false
            )

            recyclerViewOrderDetail.adapter = OrderDetailAdapter(orderedItems)

            binding.finalPrice.text = "${totalPrice} руб."
            binding.adress.text = userAddress
            binding.payMethod.text = payMethod
        }

    }

    private fun cancell() {
        binding.btnCancel.setOnClickListener {
            val position = intent.getIntExtra("ORDER_POSITION", -1)
            val itemsFromOrder = intent.getSerializableExtra("ITEMS_LIST") as? ArrayList<ItemsModel>
            if (position != -1 && itemsFromOrder != null) {
                managmentOrder.removeOrderAt(position)
                for(item in itemsFromOrder){
                    updateFire(item.id, item.numberInCart)
                }
                Toast.makeText(this, "Заказ отменён", Toast.LENGTH_SHORT).show()
                finish()
            }
            else{
                Toast.makeText(this, "Ошибка при отмене заказа", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateFire(itemId: Int, itemQuantity: Int) {
        val database = FirebaseDatabase.getInstance().getReference("Items")
        val itemRef = database.child(itemId.toString())

        itemRef.child("quantity").get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val currentStock = snapshot.getValue(Int::class.java) ?: 0
                val newStock = currentStock + itemQuantity

                itemRef.child("quantity").setValue(newStock)
                    .addOnSuccessListener {
                        Log.d("FIREBASE_WRITE", "Успешно обновлено для ID $itemId. Новый остаток: $newStock")
                    }
                    .addOnFailureListener {
                        Log.e("FIREBASE_WRITE", "Ошибка записи: ${it.message}")
                    }
            } else {
                Log.e("FIREBASE_WRITE", "Узел с ID $itemId не найден в базе по пути Items/$itemId")
            }
        }.addOnFailureListener {
            Log.e("FIREBASE_WRITE", "Ошибка получения данных: ${it.message}")
        }
    }
}
