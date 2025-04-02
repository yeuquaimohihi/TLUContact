package com.example.contact_kotlin

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText

class DonviActivity : AppCompatActivity() {
    private lateinit var rcvDonvi: RecyclerView
    private lateinit var dbHelper: ContactDatabaseHelper
    private lateinit var searchInput: TextInputEditText
    private var userRole: String = "USER"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donvi)

        dbHelper = ContactDatabaseHelper()
        rcvDonvi = findViewById(R.id.rcv_donvi)
        rcvDonvi.layoutManager = LinearLayoutManager(this)

        // Get role from intent inside onCreate
        userRole = intent.getStringExtra("USER_ROLE") ?: "USER"

        searchInput = findViewById(R.id.search_input)
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                dbHelper.searchDonvi(query) { filteredDonvis ->
                    rcvDonvi.adapter = DonviAdapter(filteredDonvis.toTypedArray())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Check for admin privileges
        if (userRole == "ADMIN") {
            findViewById<View>(R.id.btn_add_donvi).visibility = View.VISIBLE
            findViewById<View>(R.id.btn_edit_donvi).visibility = View.VISIBLE
            findViewById<View>(R.id.btn_delete_donvi).visibility = View.VISIBLE
        } else {
            findViewById<View>(R.id.btn_add_donvi).visibility = View.GONE
            findViewById<View>(R.id.btn_edit_donvi).visibility = View.GONE
            findViewById<View>(R.id.btn_delete_donvi).visibility = View.GONE
        }


        findViewById<View>(R.id.btn_add_donvi).setOnClickListener {
            val intent = Intent(this, AddDonviActivity::class.java)
            startActivity(intent)
        }

        findViewById<View>(R.id.btn_edit_donvi).setOnClickListener {
            dbHelper.getAllDonviSorted { donvis ->
                if (donvis.isEmpty()) {
                    android.app.AlertDialog.Builder(this)
                        .setTitle("Không có dữ liệu")
                        .setMessage("Không có đơn vị nào để sửa.")
                        .setPositiveButton("OK", null)
                        .show()
                } else {
                    val donviNames = donvis.map { it.name }.toTypedArray()

                    android.app.AlertDialog.Builder(this)
                        .setTitle("Chọn đơn vị để sửa")
                        .setItems(donviNames) { _, which ->
                            val selectedDonvi = donvis[which]
                            val intent = Intent(this, EditDonviActivity::class.java)
                            intent.putExtra("DONVI_ID", selectedDonvi.id)
                            startActivity(intent)
                        }
                        .setNegativeButton("Hủy", null)
                        .show()
                }
            }
        }

        findViewById<View>(R.id.btn_delete_donvi).setOnClickListener {
            dbHelper.getAllDonviSorted { donvis ->
                if (donvis.isEmpty()) {
                    android.app.AlertDialog.Builder(this)
                        .setTitle("Không có dữ liệu")
                        .setMessage("Không có đơn vị nào để xóa.")
                        .setPositiveButton("OK", null)
                        .show()
                } else {
                    val donviNames = donvis.map { it.name }.toTypedArray()

                    android.app.AlertDialog.Builder(this)
                        .setTitle("Chọn đơn vị để xóa")
                        .setItems(donviNames) { _, which ->
                            val selectedDonvi = donvis[which]

                            android.app.AlertDialog.Builder(this)
                                .setTitle("Xác nhận xóa")
                                .setMessage("Bạn có chắc chắn muốn xóa đơn vị ${selectedDonvi.name} không?")
                                .setPositiveButton("Có") { _, _ ->
                                    dbHelper.deleteDonvi(selectedDonvi.id)
                                    loadDonviData() // Reload the data
                                }
                                .setNegativeButton("Không", null)
                                .show()
                        }
                        .setNegativeButton("Hủy", null)
                        .show()
                }
            }
        }

        // Load data initially
        loadDonviData()
    }

    private fun loadDonviData() {
        dbHelper.getAllDonviSorted { donvis ->
            if (rcvDonvi.adapter == null) {
                rcvDonvi.adapter = DonviAdapter(donvis.toTypedArray())
            } else {
                (rcvDonvi.adapter as DonviAdapter).updateData(donvis.toTypedArray())
            }
        }
    }


    override fun onResume() {
        super.onResume()
        loadDonviData()
    }
}