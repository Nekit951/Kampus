package com.example.kampus2.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kampus2.activities.OrderActivity
import com.example.kampus2.activities.ProfileActivity
import com.example.kampus2.helpers.ChangeNumberItemsListener
import com.example.kampus2.R
import com.example.kampus2.helpers.ManagmentCart
import com.example.kampus2.model.ItemsModel
import com.example.kampus2.activities.BuyActivity
import com.example.kampus2.activities.MainActivity
import com.example.kampus2.adapters.CartAdapter
import com.example.kampus2.databinding.ActivityCartBinding
import com.google.firebase.database.FirebaseDatabase

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var managmentCart: ManagmentCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        managmentCart = ManagmentCart(this)

        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        initCart()
        calculatorCart()
        clickCart()
        bottom()
    }

    private fun initCart() {
        binding.apply {
            recyclerViewCart.layoutManager = LinearLayoutManager(this@CartActivity,
                LinearLayoutManager.VERTICAL, false)

            recyclerViewCart.adapter = CartAdapter(managmentCart.getListCart(), this@CartActivity,
                object: ChangeNumberItemsListener{
                    override fun onChanged() {
                        calculatorCart()
                    }
                })

            if(managmentCart.getListCart().isEmpty){
                emptyStateLayout.visibility = View.VISIBLE
                recyclerViewCart.visibility = View.GONE
                layoutCheckout.visibility = View.GONE
            }
            else{
                emptyStateLayout.visibility = View.GONE
                recyclerViewCart.visibility = View.VISIBLE
                layoutCheckout.visibility = View.VISIBLE

            }
        }
    }


    private fun calculatorCart(){
        val total = managmentCart.getTotalFee()
        binding.totalPrice.text = String.format("%.2f руб.", total)
    }

    private fun clickCart() {
        binding.btnCheckout.setOnClickListener {
            val cartItems = managmentCart.getListCart()
            if(cartItems.isNotEmpty()){
                checkQuantity(cartItems, 0)
            }
            else{
                Toast.makeText(this, "Ваша корзина пуста", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkQuantity(items: ArrayList<ItemsModel>, index: Int){
        val itemRef = items[index]
        val database = FirebaseDatabase.getInstance().getReference("Items")

        database.child(itemRef.id.toString()).child("quantity").get().addOnSuccessListener { snapshot ->
            if(snapshot.exists()){
                val currentStock = snapshot.getValue(Int::class.java) ?: 0
                if(currentStock >= itemRef.numberInCart){
                    Toast.makeText(this, "Приступайте к оформлению заказа", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, BuyActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "Недостаточное количество товаров", Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener {
            Log.e("FIREBASE_WRITE", "Ошибка получения данных: ${it.message}")
        }
    }

    private fun bottom(){
        binding.bottomNav.setOnItemSelectedListener{ menuItem ->
            when(menuItem.itemId){
                R.id.nav_cart -> true

                R.id.nav_home -> {
                    if(this !is MainActivity){
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }

                R.id.nav_order -> {
                    if(this !is OrderActivity){
                        val intent = Intent(this, OrderActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }

                R.id.nav_profile -> {
                    if(this !is ProfileActivity){
                        val intent = Intent(this, ProfileActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }

                else -> true
            }
        }

    }
}