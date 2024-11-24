package com.example.birdapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.birdapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlin.collections.Map

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        binding.textView.setOnClickListener {
            val intent = Intent(this , Registration::class.java)
            startActivity(intent)
        }
        binding.button.setOnClickListener{
            val  email = binding.emailEt.text.toString()
            val  pass = binding.passET.text.toString()


            if (email.isNotEmpty() && pass.isNotEmpty()){

                auth.createUserWithEmailAndPassword(email , pass).addOnCompleteListener{
                    if (it.isSuccessful){
                        val intent = Intent(this , Map::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
                val intent = Intent(this , Map::class.java)
                startActivity(intent)
            }else {
                Toast.makeText(this, "Empty field are not allowed", Toast.LENGTH_SHORT).show()
            }


        }

    }
}

