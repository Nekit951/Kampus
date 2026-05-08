package com.example.kampus2.activities

import android.content.Intent
import android.location.Address
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kampus2.activities.OrderActivity
import com.example.kampus2.adapters.BuyAdapter
import com.example.kampus2.databinding.ActivityBuyBinding
import com.example.kampus2.helpers.ManagmentCart
import com.example.kampus2.helpers.ManagmentOrder
import com.example.kampus2.model.Card
import com.example.kampus2.model.CardDB
import com.example.kampus2.model.ItemsModel
import com.example.kampus2.model.OrderModel
import com.google.firebase.database.FirebaseDatabase

class BuyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuyBinding
    private lateinit var managmentCart: ManagmentCart
    private lateinit var managmentOrder: ManagmentOrder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBuyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        managmentCart = ManagmentCart(this)
        managmentOrder = ManagmentOrder(this)


        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        initBuy()
        calculatorBuy()
        clickBuy()
    }

    private fun initBuy() {
        val cartItems = managmentCart.getListCart()

        binding.apply {
            recyclerViewBuy.layoutManager = LinearLayoutManager(this@BuyActivity,
                LinearLayoutManager.VERTICAL, false)

            recyclerViewBuy.adapter = BuyAdapter(cartItems, this@BuyActivity)
        }
    }

    private fun calculatorBuy(){
        val deliverPrice = 100.0
        val totalAmount = managmentCart.getTotalFee()
        val finalPrice = totalAmount + deliverPrice
        binding.itemPrice.text = "$totalAmount руб."
        binding.deliveryPrice.text = "$deliverPrice руб."
        binding.finalPrice.text = "$finalPrice руб."
    }

    private fun clickBuy() {
        binding.btnClick.setOnClickListener{
            val cartItems = managmentCart.getListCart()
            val userAdress = binding.adress.text.toString().trim()
            val finalPriceValue = managmentCart.getTotalFee() + 100.0
            val selectedId = binding.rgPayment.checkedRadioButtonId
            if(selectedId != -1){
                findViewById<RadioButton>(selectedId).text.toString()
            }
            else{
                Toast.makeText(this, "Выберите способ оплаты", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val payMethod = findViewById<RadioButton>(selectedId).text.toString()
            val card = CardDB(this, null)
            val saveCard = card.getCard()
            if(payMethod == "Банковской картой онлайн" && saveCard.isEmpty()){
                Toast.makeText(this, "Добавьте свою банковскую карту", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(userAdress.isEmpty()){
                Toast.makeText(this, "Введите адрес", Toast.LENGTH_SHORT).show()
            }

            val order = OrderModel(
                items = ArrayList(cartItems),
                summa = finalPriceValue,
                address = userAdress,
                payMethod = payMethod
            )

            managmentOrder.insertOrder(order)

            for(cartItem in cartItems){
                updateFire(cartItem.id, cartItem.numberInCart)
            }

            val intent = Intent(this, OrderActivity::class.java)
            managmentCart.clearCart()
            startActivity(intent)
            finish()
        }
    }

    private fun updateFire(itemId: Int, itemQuantity: Int) {
        val database = FirebaseDatabase.getInstance().getReference("Items")
        val itemRef = database.child(itemId.toString())

        itemRef.child("quantity").get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val currentStock = snapshot.getValue(Int::class.java) ?: 0
                val newStock = (currentStock - itemQuantity).coerceAtLeast(0)

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