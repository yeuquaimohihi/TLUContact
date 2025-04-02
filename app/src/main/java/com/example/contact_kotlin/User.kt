package com.example.contact_kotlin

data class User(
    val id: String = "",
    val username: String = "",
    val password: String = "", // Add this field
    val role: String = ""
)
enum class UserRole {
    ADMIN,
    USER
}