package com.example.kampus2.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.kampus2.activities.ProfileActivity
import com.example.kampus2.R
import com.example.kampus2.databinding.ActivityDescBinding
import com.example.kampus2.helpers.ManagmentCart
import com.example.kampus2.model.ItemsModel

class DescActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDescBinding
    private lateinit var item: ItemsModel
    private lateinit var managmentCart: ManagmentCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDescBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        managmentCart = ManagmentCart(this)
        item = intent.getSerializableExtra("object")!! as ItemsModel

        setUpViews()
        addToCart()
        bottom()
    }

    private fun setUpViews() = with(binding) {
        itemTitle.text = item.title
        itemPrice.text = "${item.price} руб."
        itemQuantity.text = "${item.quantity} шт."

        Glide.with(this@DescActivity).load(item.picUrl).into(picMain)
    }

    private fun addToCart(){
        binding.buttonCart.setOnClickListener {
            item.numberInCart = 1
            managmentCart.insertFood(item)
            val intent = Intent(this@DescActivity, CartActivity::class.java)
            startActivity(intent)
        }
    }

    private fun bottom(){
        binding.bottomNav.setOnItemSelectedListener{ menuItem ->
            when(menuItem.itemId){
                R.id.nav_home -> {
                    if(this !is MainActivity){
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }

                R.id.nav_cart -> {
                    if(this !is CartActivity){
                        val intent = Intent(this, CartActivity::class.java)
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

                else -> false
            }
        }
    }

}