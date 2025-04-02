package com.example.contact_kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.contact_kotlin.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var userRole: String = "USER" // Default role
    private lateinit var dbHelper: ContactDatabaseHelper
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth
        dbHelper = ContactDatabaseHelper()

        // Get role from intent
        userRole = intent.getStringExtra("USER_ROLE") ?: "USER"

        binding.btnDonvi.setOnClickListener {
            val intent = Intent(this, DonviActivity::class.java)
            intent.putExtra("USER_ROLE", userRole)
            startActivity(intent)
        }

        binding.btnCBNV.setOnClickListener {
            val intent = Intent(this, CbnvActivity::class.java)
            intent.putExtra("USER_ROLE", userRole)
            startActivity(intent)
        }

        binding.btnAddUser.setOnClickListener {
            // Handle add user functionality
        }

        binding.btnLogout.setOnClickListener {
            // Sign out the user before redirecting to login
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // User not logged in, redirect to login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}