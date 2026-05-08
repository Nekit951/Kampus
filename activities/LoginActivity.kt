package com.example.kampus2.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.kampus2.R
import com.example.kampus2.model.DbHelper

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val userEmail: EditText = findViewById(R.id.Email)
        val userPass: EditText = findViewById(R.id.Pass)
        val btn: Button = findViewById(R.id.ButtonLogin)

        btn.setOnClickListener {
            val email = userEmail.text.toString().trim()
            val pass = userPass.text.toString().trim()

            if(email == "" || pass == ""){
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            }
            else{
                val db = DbHelper(this, null)
                val user = db.getUser(email, pass)

                if(user != null){
                    val sharedPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                    with(sharedPrefs.edit()){
                        putBoolean("isLoggedIn", true)
                        putString("current_user_email", email)
                        putString("current_user_pass", pass)
                        apply()
                    }
                    Toast.makeText(this, "Пользователь авторизован", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                    userEmail.text.clear()
                    userPass.text.clear()
                }
                else{
                    Toast.makeText(this, "Неверный email и пароль", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}