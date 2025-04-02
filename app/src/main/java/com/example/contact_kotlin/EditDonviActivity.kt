package com.example.contact_kotlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class EditDonviActivity : AppCompatActivity() {
    private lateinit var dbHelper: ContactDatabaseHelper
    private var donviId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_donvi)

        dbHelper = ContactDatabaseHelper()

        val nameInput = findViewById<EditText>(R.id.input_name)
        val phoneInput = findViewById<EditText>(R.id.input_phone)
        val addressInput = findViewById<EditText>(R.id.input_address)
        val editButton = findViewById<Button>(R.id.btn_edit)

        donviId = intent.getIntExtra("DONVI_ID", -1)
        dbHelper.getDonviById(donviId) { donvi ->
            nameInput.setText(donvi.name)
            phoneInput.setText(donvi.phone)
            addressInput.setText(donvi.address)
        }

        editButton.setOnClickListener {
            val name = nameInput.text.toString()
            val phone = phoneInput.text.toString()
            val address = addressInput.text.toString()

            val updatedDonvi = Donvi(
                donviId,
                name,
                phone,
                address,
                R.drawable.ic_launcher_background
            )

            dbHelper.updateDonvi(updatedDonvi)

            // Create an intent to pass back the command to sort by name
            val resultIntent = Intent()
            resultIntent.putExtra("SORT_BY_NAME", true)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}