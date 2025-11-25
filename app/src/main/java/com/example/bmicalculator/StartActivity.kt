package com.example.bmicalculator

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bmicalculator.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    private lateinit var b: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityStartBinding.inflate(layoutInflater)
        setContentView(b.root)

        // Butona tıklayınca hesaplama ekranına geç
        b.btnGo.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish() // geri tuşuna basınca tekrar girişe dönmesin
        }
    }
}
