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

class CbnvActivity : AppCompatActivity() {
    private lateinit var rcvCbnv: RecyclerView
    private lateinit var dbHelper: ContactDatabaseHelper
    private lateinit var searchInput: TextInputEditText
    private var userRole: String = "USER"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cbnv)

        // Get role from intent INSIDE onCreate
        userRole = intent.getStringExtra("USER_ROLE") ?: "USER"

        dbHelper = ContactDatabaseHelper()
        rcvCbnv = findViewById(R.id.rcv_cbnv)
        rcvCbnv.layoutManager = LinearLayoutManager(this)

        searchInput = findViewById(R.id.search_input)
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                dbHelper.searchCbnvs(query) { filteredCbnvs ->
                    rcvCbnv.adapter = CbnvAdapter(filteredCbnvs.toTypedArray())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Show admin controls if user has admin role
        if (userRole == "ADMIN") {
            findViewById<View>(R.id.btn_add_cbnv).visibility = View.VISIBLE
            findViewById<View>(R.id.btn_edit_cbnv).visibility = View.VISIBLE
            findViewById<View>(R.id.btn_delete_cbnv).visibility = View.VISIBLE
        } else {
            findViewById<View>(R.id.btn_add_cbnv).visibility = View.GONE
            findViewById<View>(R.id.btn_edit_cbnv).visibility = View.GONE
            findViewById<View>(R.id.btn_delete_cbnv).visibility = View.GONE
        }



        findViewById<View>(R.id.btn_add_cbnv).setOnClickListener {
            val intent = Intent(this, AddCbnvActivity::class.java)
            startActivity(intent)
        }

        findViewById<View>(R.id.btn_edit_cbnv).setOnClickListener {
            dbHelper.getAllCbnvSorted { cbnvs ->
                if (cbnvs.isEmpty()) {
                    android.app.AlertDialog.Builder(this)
                        .setTitle("Không có dữ liệu")
                        .setMessage("Không có nhân viên nào để sửa.")
                        .setPositiveButton("OK", null)
                        .show()
                } else {
                    val cbnvNames = cbnvs.map { it.nameCbnv }.toTypedArray()

                    android.app.AlertDialog.Builder(this)
                        .setTitle("Chọn nhân viên để sửa")
                        .setItems(cbnvNames) { _, which ->
                            val selectedCbnv = cbnvs[which]
                            val intent = Intent(this, EditCbnvActivity::class.java)
                            intent.putExtra("CBNV_ID", selectedCbnv.id)
                            startActivity(intent)
                        }
                        .setNegativeButton("Hủy", null)
                        .show()
                }
            }
        }

        findViewById<View>(R.id.btn_delete_cbnv).setOnClickListener {
            dbHelper.getAllCbnvSorted { cbnvs ->
                if (cbnvs.isEmpty()) {
                    android.app.AlertDialog.Builder(this)
                        .setTitle("Không có dữ liệu")
                        .setMessage("Không có nhân viên nào để xóa.")
                        .setPositiveButton("OK", null)
                        .show()
                } else {
                    val cbnvNames = cbnvs.map { it.nameCbnv }.toTypedArray()

                    android.app.AlertDialog.Builder(this)
                        .setTitle("Chọn nhân viên để xóa")
                        .setItems(cbnvNames) { _, which ->
                            val selectedCbnv = cbnvs[which]

                            android.app.AlertDialog.Builder(this)
                                .setTitle("Xác nhận xóa")
                                .setMessage("Bạn có chắc chắn muốn xóa nhân viên ${selectedCbnv.nameCbnv} không?")
                                .setPositiveButton("Có") { _, _ ->
                                    dbHelper.deleteCbnv(selectedCbnv.id)
                                    loadCbnvData() // Reload the data
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
        loadCbnvData()
    }

    private fun loadCbnvData() {
        dbHelper.getAllCbnvSorted { cbnvs ->
            if (rcvCbnv.adapter == null) {
                rcvCbnv.adapter = CbnvAdapter(cbnvs.toTypedArray())
            } else {
                (rcvCbnv.adapter as CbnvAdapter).updateData(cbnvs.toTypedArray())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadCbnvData() // Refresh data when activity resumes
    }
}