package com.example.contact_kotlin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

class AddDonviActivity : AppCompatActivity() {

    private lateinit var dbHelper: ContactDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_donvi)

        dbHelper = ContactDatabaseHelper()

        findViewById<Button>(R.id.btn_add).setOnClickListener {
            addDonvi()
        }
    }

    private fun addDonvi() {
        val name = findViewById<EditText>(R.id.input_name).text.toString().trim()
        val phone = findViewById<EditText>(R.id.input_phone).text.toString().trim()
        val address = findViewById<EditText>(R.id.input_address).text.toString().trim()

        // Validate inputs
        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }

        // Generate a new ID (using timestamp for uniqueness)
        val id = System.currentTimeMillis().toInt()

        val donvi = Donvi(
            id = id,
            name = name,
            phone = phone,
            address = address,
            imageResource = R.drawable.ic_business // Default image
        )

        // Add to Firestore
        dbHelper.addDonvi(donvi)

        Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show()

        // Return to previous activity
        finish()
    }
}