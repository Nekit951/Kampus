package com.example.kampus2.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.kampus2.activities.OrderActivity
import com.example.kampus2.activities.ProfileActivity
import com.example.kampus2.R
import com.example.kampus2.model.DbHelper
import com.google.android.material.bottomnavigation.BottomNavigationView

class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)

        val Fio: TextView = findViewById(R.id.et_fio)
        val Email: TextView = findViewById(R.id.et_email)
        val Pass: TextView = findViewById(R.id.et_pass)
        val Back: ImageView = findViewById(R.id.backBtn)
        val Edit: Button = findViewById(R.id.btn_edit)
        val bottom: BottomNavigationView = findViewById(R.id.bottom_nav)

        val sharedPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val dbHelper = DbHelper(this, null)

        val oldEmail = sharedPrefs.getString("current_user_email", "") ?: ""
        val oldPass = sharedPrefs.getString("current_user_pass", "") ?: ""

        if(oldEmail.isEmpty()){
            finish()
            return
        }

        val user = dbHelper.getUser(oldEmail, oldPass)
        if(user != null){
            Fio.setText(user.fio)
            Email.setText(user.email)
            Pass.setText(user.pass)
        }

        Back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        Edit.setOnClickListener {
            val newFio = Fio.text.toString().trim()
            val newEmail = Email.text.toString().trim()
            val newPass = Pass.text.toString().trim()

            if(newFio.isEmpty()){
                Fio.error =  "Введите ФИО";
                return@setOnClickListener
            }
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()){
                Email.error =  "Некорректный email";
                return@setOnClickListener
            }
            if(newPass.length < 6){
                Pass.error =  "Пароль от 6 символов";
                return@setOnClickListener
            }

            try {
                val db = dbHelper.writableDatabase
                val values = android.content.ContentValues().apply {
                    put("fio", newFio)
                    put("email", newEmail)
                    put("pass", newPass)
                }

                db.update("users", values, "email=? AND pass=?", arrayOf(oldEmail, oldPass))

                val editor = sharedPrefs.edit()
                editor.putString("current_user_email", newEmail)
                editor.putString("current_user_pass", newPass)
                editor.apply()

                Toast.makeText(this, "Данные успешно обновлены", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                finish()
            }
            catch (e: Exception){
                Toast.makeText(this, "Ошибка: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        bottom.setOnItemSelectedListener{ menuItem ->
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