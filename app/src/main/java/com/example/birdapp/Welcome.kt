package com.example.birdapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Welcome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val buttonBtnOne = findViewById<Button>(R.id.buttonBtnOne)
        buttonBtnOne.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent);
        }
    }
}