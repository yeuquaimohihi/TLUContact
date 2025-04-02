// DBHelper.kt
package com.example.contact_kotlin

import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log



class DBHelper {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun addUser(user: User) {
        firestore.collection("users").document(user.id).set(user)
    }

    fun getUser(username: String, callback: (User?) -> Unit) {
        firestore.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val user = documents.first().toObject(User::class.java)
                    callback(user)
                } else {
                    callback(null)
                }
            }
    }

    fun authenticateUser(username: String, password: String, callback: (User?) -> Unit) {
        // Log raw inputs to verify what's being sent
        Log.d("DBHelper", "Authenticating with username: '$username', password: '$password'")

        firestore.collection("users")
            .get()
            .addOnSuccessListener { documents ->
                Log.d("DBHelper", "All users query returned ${documents.size()} documents")

                // Log all usernames to check what's in the database
                documents.forEach { doc ->
                    Log.d("DBHelper", "Database username: '${doc.getString("username")}'")
                }

                // Now perform the actual authentication
                firestore.collection("user")
                    .whereEqualTo("username", username.trim())
                    .get()
                    .addOnSuccessListener { matchingUsers ->
                        if (matchingUsers.isEmpty) {
                            Log.d("DBHelper", "No user found with username: '$username'")
                            callback(null)
                        } else {
                            val userDoc = matchingUsers.documents[0]
                            val storedPassword = userDoc.getString("password")
                            Log.d("DBHelper", "Found user. Stored password: '$storedPassword', Input password: '$password'")

                            if (storedPassword == password) {
                                val user = userDoc.toObject(User::class.java)
                                Log.d("DBHelper", "Password matched, authentication successful")
                                callback(user)
                            } else {
                                Log.d("DBHelper", "Password didn't match")
                                callback(null)
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("DBHelper", "Username query error", e)
                        callback(null)
                    }
            }
            .addOnFailureListener { e ->
                Log.e("DBHelper", "Database query error", e)
                callback(null)
            }
    }
}