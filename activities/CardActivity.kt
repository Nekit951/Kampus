package com.example.kampus2.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kampus2.activities.CardAddActivity
import com.example.kampus2.activities.CartActivity
import com.example.kampus2.activities.OrderActivity
import com.example.kampus2.activities.ProfileActivity
import com.example.kampus2.adapters.CardAdapter
import com.example.kampus2.model.CardDB
import com.example.kampus2.R
import com.example.kampus2.databinding.ActivityCardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCardBinding
    private lateinit var cardAdapter: CardAdapter
    private lateinit var db: CardDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.addCardView.setOnClickListener{
            val intent = Intent(this, CardAddActivity::class.java)
            startActivity(intent)
        }

        db = CardDB(this, null)

        bottom()
        cardInit()
    }

    private fun cardInit() {
        binding.apply {
            recyclerViewCard.layoutManager = LinearLayoutManager(this@CardActivity,
                LinearLayoutManager.VERTICAL, false)

            val cardList = db.getCard()
            cardAdapter = CardAdapter(cardList)

            recyclerViewCard.adapter = cardAdapter


        }
    }

    private fun bottom() {
        binding.bottomNav.setOnItemSelectedListener { menuItem ->
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