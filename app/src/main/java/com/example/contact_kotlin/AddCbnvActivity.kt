package com.example.contact_kotlin

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class AddCbnvActivity : AppCompatActivity() {
    private lateinit var dbHelper: ContactDatabaseHelper
    private lateinit var donviInput: EditText
    private var selectedDonviId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_cbnv)

        dbHelper = ContactDatabaseHelper()

        val nameInput = findViewById<EditText>(R.id.input_name)
        val chucvuInput = findViewById<EditText>(R.id.input_chucvu)
        val phoneInput = findViewById<EditText>(R.id.input_phone)
        val emailInput = findViewById<EditText>(R.id.input_email)
        donviInput = findViewById<EditText>(R.id.input_donvi)
        val addButton = findViewById<Button>(R.id.btn_add)

        // Configure the donviInput to be selectable from a list
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

        addButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val chucvu = chucvuInput.text.toString().trim()
            val phone = phoneInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val donvi = donviInput.text.toString().trim()

            // Validate inputs
            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập ít nhất họ tên và số điện thoại", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Generate a new unique ID
            val id = System.currentTimeMillis().toInt()

            val newCbnv = Cbnv(
                id = id,
                nameCbnv = name,
                positionCbnv = chucvu,
                phoneCbnv = phone,
                emailCbnv = email,
                donviId = selectedDonviId,
                avatarCbnv = R.drawable.ic_add_person // Default avatar
            )

            // Add to Firestore with callback
            dbHelper.addCbnv(newCbnv, { success ->
                if (success) {
                    Log.d("AddCbnvActivity", "CBNV added successfully with ID: $id")
                    Toast.makeText(this, "Thêm cán bộ nhân viên thành công", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Log.e("AddCbnvActivity", "Failed to add CBNV")
                    Toast.makeText(this, "Có lỗi xảy ra khi thêm cán bộ nhân viên", Toast.LENGTH_SHORT).show()
                }
            })
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
                    // Store the donvi ID for later use when creating the CBNV
                    selectedDonviId = donvis[which].id
                    Log.d("AddCbnvActivity", "Selected donviId: $selectedDonviId")
                }
                .setNegativeButton("Hủy", null)
                .show()
        }
    }
}