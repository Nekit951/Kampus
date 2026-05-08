package com.example.kampus2.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.kampus2.R

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_intro)

        val intro = 2500L

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, AccountOptionsActivity::class.java)
            startActivity(intent)
            finish()
        }, intro)

    }
}