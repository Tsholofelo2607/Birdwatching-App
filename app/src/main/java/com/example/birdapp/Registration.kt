package com.example.birdapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.birdapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Registration : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference


        binding.textView.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {

                            val intent = Intent(this, Login::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun saveUserData(userId: String, pass: String, confirmPass: String, email: String) {
        val user = User(pass, confirmPass, email)
        database.child("users").child(userId).setValue(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                    // Navigate to login screen
                    startActivity(Intent(this, Login::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Failed to save user data: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

data class User(val pass: String, val confirmPass: String, val email: String)


