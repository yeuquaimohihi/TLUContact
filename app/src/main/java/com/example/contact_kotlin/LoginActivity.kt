package com.example.contact_kotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.contact_kotlin.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.btnLogin.setOnClickListener {
            val email = binding.editEmail.text.toString().trim()
            val password = binding.editPassword.text.toString().trim()

            if (validateForm(email, password)) {
                loginUser(email, password)
            }
        }

        binding.txtRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.txtForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    if (user != null) {
                        // Check if email is verified
                        if (user.isEmailVerified) {
                            // User is verified, proceed to main app
                            checkUserRoleAndNavigate(user.uid, email)
                        } else {
                            // User is not verified, redirect to verification activity
                            val intent = Intent(this, VerificationActivity::class.java)
                            startActivity(intent)
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Đăng nhập thất bại: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun checkUserRoleAndNavigate(userId: String, email: String) {
        val dbHelper = ContactDatabaseHelper()
        dbHelper.getUserRole(userId) { role ->
            if (role == null) {
                // If role is not set, check if email should be admin
                val newRole = if (isAdminEmail(email)) "ADMIN" else "USER"
                dbHelper.saveUserRole(userId, email, newRole) { _ ->
                    navigateToMainActivity(newRole)
                }
            } else {
                navigateToMainActivity(role)
            }
        }
    }

    private fun showVerificationDialog() {
        android.app.AlertDialog.Builder(this)
            .setTitle("Email chưa được xác thực")
            .setMessage("Vui lòng xác thực email của bạn trước khi đăng nhập. Bạn có muốn gửi lại email xác thực không?")
            .setPositiveButton("Gửi lại") { _, _ ->
                auth.currentUser?.sendEmailVerification()
                    ?.addOnCompleteListener { verificationTask ->
                        if (verificationTask.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Đã gửi lại email xác thực. Vui lòng kiểm tra hộp thư của bạn.",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                this,
                                "Không thể gửi email xác thực: ${verificationTask.exception?.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
            .setNegativeButton("Hủy") { _, _ ->
                // Sign out since we don't want unverified users to remain logged in
                auth.signOut()
            }
            .setCancelable(false)
            .show()
    }

    private fun navigateToMainActivity(role: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("USER_ROLE", role) // Changed from "role" to "USER_ROLE"
        Toast.makeText(this, "Đăng nhập thành công với vai trò: $role", Toast.LENGTH_SHORT).show()
        startActivity(intent)
        finish()
    }


    // Helper function to check if email should be admin
    private fun isAdminEmail(email: String): Boolean {
        // List of admin emails
        val adminEmails = listOf(

            "admin@tlu.edu.vn"
            // Add more admin emails as needed
        )
        return adminEmails.contains(email.lowercase())
    }


}