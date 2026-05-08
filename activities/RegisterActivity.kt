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
import com.example.kampus2.model.User

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        val userFio: EditText = findViewById(R.id.FIO)
        val userEmail: EditText = findViewById(R.id.Email)
        val userPass: EditText = findViewById(R.id.Pass)
        val btn: Button = findViewById(R.id.ButtonRegister)

        btn.setOnClickListener {
            val fio = userFio.text.toString().trim()
            val email = userEmail.text.toString().trim()
            val pass = userPass.text.toString().trim()

            if(fio == "" || email == "" || pass == ""){
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            }
            else{
                val user = User(fio, email, pass)

                val db = DbHelper(this, null)
                db.addUser(user)

                userFio.text.clear()
                userEmail.text.clear()
                userPass.text.clear()

                val sharedPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                with(sharedPrefs.edit()){
                    putBoolean("isLoggedIn", true)
                    putString("current_user_email", email)
                    putString("current_user_pass", pass)
                    apply()
                }
                Toast.makeText(this, "Пользователь добавлен", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}