package com.example.kampus2.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.kampus2.activities.OrderActivity
import com.example.kampus2.R
import com.example.kampus2.model.DbHelper
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        val Fio: TextView = findViewById(R.id.fio_profile)
        val Email: TextView = findViewById(R.id.email_profile)
        val Pass: TextView = findViewById(R.id.pass_profile)
        val back: ImageView = findViewById(R.id.backBtn)
        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_nav)
        val edit: Button = findViewById(R.id.btn_edit)
        val exit: Button = findViewById(R.id.btn_log)

        val sharedPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        val savedEmail = sharedPrefs.getString("current_user_email", "") ?: ""
        val savedPass = sharedPrefs.getString("current_user_pass", "") ?: ""

        val dbHelper = DbHelper(this, null)
        val user = dbHelper.getUser(savedEmail, savedPass!!)

        if(user != null){
            Fio.text = user.fio
            Email.text = user.email
            Pass.text = user.pass
        }
        else{
            Email.text = savedEmail
            Pass.text = savedPass
        }

        back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        edit.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        exit.setOnClickListener {
            val editor = sharedPrefs.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> true

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

                else -> false
            }
        }
    }
}