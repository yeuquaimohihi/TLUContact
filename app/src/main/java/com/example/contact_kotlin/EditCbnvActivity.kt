package com.example.contact_kotlin

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.util.Log

class EditCbnvActivity : AppCompatActivity() {
    private lateinit var dbHelper: ContactDatabaseHelper
    private var cbnvId: Int = -1
    private lateinit var donviInput: EditText
    private var selectedDonviId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_cbnv)

        dbHelper = ContactDatabaseHelper()

        val nameInput = findViewById<EditText>(R.id.input_name)
        val positionInput = findViewById<EditText>(R.id.input_chucvu)
        val phoneInput = findViewById<EditText>(R.id.input_phone)
        val emailInput = findViewById<EditText>(R.id.input_email)
        donviInput = findViewById<EditText>(R.id.input_donvi)
        val editButton = findViewById<Button>(R.id.btn_edit)

        cbnvId = intent.getIntExtra("CBNV_ID", -1)

        // Get CBNV by ID and populate fields
        dbHelper.getCbnvById(cbnvId) { cbnv ->
            nameInput.setText(cbnv.nameCbnv)
            positionInput.setText(cbnv.positionCbnv)
            phoneInput.setText(cbnv.phoneCbnv)
            emailInput.setText(cbnv.emailCbnv)
            selectedDonviId = cbnv.donviId

            // Get the donvi name based on the ID
            if (selectedDonviId > 0) {
                dbHelper.getDonviById(selectedDonviId) { donvi ->
                    donviInput.setText(donvi.name)
                }
            }
        }

        // Configure the donviInput
        donviInput.apply {
            isFocusable = false
            isClickable = true
            isCursorVisible = false
            keyListener = null
            setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0)
            setOnClickListener {
                showDonviSelectionDialog()
            }
        }

        editButton.setOnClickListener {
            val name = nameInput.text.toString()
            val position = positionInput.text.toString()
            val phone = phoneInput.text.toString()
            val email = emailInput.text.toString()

            val updatedCbnv = Cbnv(
                id = cbnvId,
                nameCbnv = name,
                positionCbnv = position,
                phoneCbnv = phone,
                emailCbnv = email,
                donviId = selectedDonviId,
                avatarCbnv = R.drawable.ic_people
            )

            dbHelper.updateCbnv(updatedCbnv)
            finish()
        }
    }

    private fun showDonviSelectionDialog() {
        dbHelper.getAllDonviSorted { donvis ->
            if (donvis.isEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("Không có dữ liệu")
                    .setMessage("Không có đơn vị nào để chọn.")
                    .setPositiveButton("OK", null)
                    .show()
                return@getAllDonviSorted
            }

            // Create a list of donvi names for the dialog
            val donviNames = donvis.map { it.name }.toTypedArray()

            // Show dialog to select which donvi
            AlertDialog.Builder(this)
                .setTitle("Chọn đơn vị")
                .setItems(donviNames) { _: DialogInterface, which: Int ->
                    // Set the selected donvi name to the input field
                    donviInput.setText(donviNames[which])
                    // Store the donvi ID for later use
                    selectedDonviId = donvis[which].id
                    Log.d("EditCbnvActivity", "Selected donviId: $selectedDonviId")
                }
                .setNegativeButton("Hủy", null)
                .show()
        }
    }
}