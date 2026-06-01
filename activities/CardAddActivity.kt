package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityCardAddBinding
import com.example.myapplication.model.Card
import com.example.myapplication.model.CardDB

class CardAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCardAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCardAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setUp()
        buttonAdd()
        bottom()
    }

    private fun setUp(){
        binding.etNumber.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        binding.etData.inputType = android.text.InputType.TYPE_CLASS_NUMBER

        binding.etNumber.addTextChangedListener(CardNumberText(binding.etNumber))
        binding.etData.addTextChangedListener(DataNumberText(binding.etData))
    }

    private fun buttonAdd() {
        binding.btnAddCard.setOnClickListener {
            val number = binding.etNumber.text.toString().trim()
            val data = binding.etData.text.toString().trim()
            val cvp = binding.etCVP.text.toString().trim()


            if(number == "" || data == "" || cvp == ""){
                Toast.makeText(this, "Заполните поля", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(number.length != 19){
                Toast.makeText(this, "Неверный номер карты", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(data.length != 5){
                Toast.makeText(this, "Неверный формат даты", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(cvp.length != 3){
                Toast.makeText(this, "Неверный формат CVP", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val newCard = Card(number, data, cvp)
            val db = CardDB(this, null)
            db.addCard(newCard)

            Toast.makeText(this, "Карта добавлена", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, CardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun bottom() {
        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    if (this !is MainActivity) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }

                R.id.nav_cart -> {
                    if (this !is CartActivity) {
                        val intent = Intent(this, CartActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }

                R.id.nav_order -> {
                    if (this !is OrderActivity) {
                        val intent = Intent(this, OrderActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }

                R.id.nav_profile -> {
                    if (this !is ProfileActivity) {
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
