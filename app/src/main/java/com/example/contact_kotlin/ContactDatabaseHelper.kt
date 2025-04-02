// ContactDatabaseHelper.kt
package com.example.contact_kotlin

import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
// Add imports for Donvi and Cbnv classes from their actual location

class ContactDatabaseHelper {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val DONVI_COLLECTION = "donvi"
    private val CBNV_COLLECTION = "cbnv"

    fun addDonvi(donvi: Donvi, callback: ((Boolean) -> Unit)? = null) {
        firestore.collection(DONVI_COLLECTION).document(donvi.id.toString())
            .set(donvi)
            .addOnSuccessListener {
                Log.d("DBHelper", "Document successfully added with ID: ${donvi.id}")
                callback?.invoke(true)
            }
            .addOnFailureListener { e ->
                Log.e("DBHelper", "Error adding document", e)
                callback?.invoke(false)
            }
    }

    fun addCbnv(cbnv: Cbnv, callback: ((Boolean) -> Unit)? = null) {
        val CBNV_COLLECTION = "cbnv"
        firestore.collection(CBNV_COLLECTION).document(cbnv.id.toString())
            .set(cbnv)
            .addOnSuccessListener {
                Log.d("DBHelper", "CBNV successfully added with ID: ${cbnv.id}")
                callback?.invoke(true)
            }
            .addOnFailureListener { e ->
                Log.e("DBHelper", "Error adding CBNV", e)
                callback?.invoke(false)
            }
    }

    fun getAllDonviSorted(callback: (List<Donvi>) -> Unit) {
        firestore.collection(DONVI_COLLECTION)
            .orderBy("name")
            .get()
            .addOnSuccessListener { documents ->
                Log.d("DBHelper", "Retrieved ${documents.size()} documents")
                val donviList = documents.mapNotNull { doc ->
                    try {
                        val donvi = doc.toObject(Donvi::class.java)
                        Log.d("DBHelper", "Converted document to Donvi: ${donvi.name}")
                        donvi
                    } catch (e: Exception) {
                        Log.e("DBHelper", "Error converting document: ${doc.id}", e)
                        null
                    }
                }
                Log.d("DBHelper", "Returning ${donviList.size} items")
                callback(donviList)
            }
            .addOnFailureListener { e ->
                Log.e("DBHelper", "Error getting sorted donvis", e)
                callback(emptyList())
            }
    }

    fun getAllCbnvSorted(callback: (List<Cbnv>) -> Unit) {
        firestore.collection("cbnv")
            .orderBy("nameCbnv")  // Using the field name from your code
            .get()
            .addOnSuccessListener { documents ->
                val cbnvList = documents.map { it.toObject(Cbnv::class.java) }
                callback(cbnvList)
            }
    }

    fun searchCbnvs(query: String, callback: (List<Cbnv>) -> Unit) {
        firestore.collection("cbnv")
            .orderBy("nameCbnv")
            .get()
            .addOnSuccessListener { documents ->
                try {
                    val cbnvList = documents.mapNotNull { document ->
                        try {
                            val cbnv = document.toObject(Cbnv::class.java)
                            if (cbnv.nameCbnv.contains(query, ignoreCase = true)) cbnv else null
                        } catch (e: Exception) {
                            Log.e("DBHelper", "Error converting document: ${document.id}", e)

                            // Alternative manual conversion using document data
                            val data = document.data
                            val id = (data?.get("id") as? String)?.toIntOrNull()
                                ?: document.id.toIntOrNull()
                                ?: -1

                            val name = data?.get("nameCbnv") as? String ?: ""
                            val position = data?.get("positionCbnv") as? String ?: ""
                            val phone = data?.get("phoneCbnv") as? String ?: ""
                            val email = data?.get("emailCbnv") as? String ?: ""
                            val donviId = (data?.get("donviId") as? Number)?.toInt() ?: -1
                            val avatar = (data?.get("avatarCbnv") as? Number)?.toInt() ?: R.drawable.ic_people

                            if (name.contains(query, ignoreCase = true)) {
                                Cbnv(id, name, position, phone, email, donviId, avatar)
                            } else null
                        }
                    }
                    callback(cbnvList)
                } catch (e: Exception) {
                    Log.e("DBHelper", "Error in searchCbnvs", e)
                    callback(emptyList())
                }
            }
            .addOnFailureListener { e ->
                Log.e("DBHelper", "Error getting documents in searchCbnvs", e)
                callback(emptyList())
            }
    }

    fun updateCbnv(cbnv: Cbnv, callback: ((Boolean) -> Unit)? = null) {
        firestore.collection(CBNV_COLLECTION).document(cbnv.id.toString())
            .set(cbnv)
            .addOnSuccessListener {
                Log.d("DBHelper", "CBNV updated successfully with donviId: ${cbnv.donviId}")
                callback?.invoke(true)
            }
            .addOnFailureListener { e ->
                Log.e("DBHelper", "Error updating CBNV", e)
                callback?.invoke(false)
            }
    }

    fun getCbnvById(id: Int, callback: (Cbnv) -> Unit) {
        firestore.collection("cbnv")
            .document(id.toString())
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val cbnv = document.toObject(Cbnv::class.java)
                    if (cbnv != null) {
                        callback(cbnv)
                    }
                }
            }
    }

    fun deleteCbnv(id: Int) {
        val CBNV_COLLECTION = "cbnv"
        firestore.collection(CBNV_COLLECTION).document(id.toString()).delete()
            .addOnSuccessListener {
                Log.d("DBHelper", "CBNV successfully deleted with ID: $id")
            }
            .addOnFailureListener { e ->
                Log.e("DBHelper", "Error deleting CBNV", e)
            }
    }
    fun searchDonvi(query: String, callback: (List<Donvi>) -> Unit) {
        firestore.collection(DONVI_COLLECTION)
            .orderBy("name")
            .get()
            .addOnSuccessListener { documents ->
                try {
                    val donviList = documents.mapNotNull { document ->
                        try {
                            val donvi = document.toObject(Donvi::class.java)
                            if (donvi.name.contains(query, ignoreCase = true)) donvi else null
                        } catch (e: Exception) {
                            Log.e("DBHelper", "Error converting document: ${document.id}", e)

                            // Alternative manual conversion if needed
                            val data = document.data
                            val id = (data?.get("id") as? String)?.toIntOrNull()
                                ?: document.id.toIntOrNull()
                                ?: -1

                            val name = data?.get("name") as? String ?: ""
                            val phone = data?.get("phone") as? String ?: ""
                            val address = data?.get("address") as? String ?: ""

                            if (name.contains(query, ignoreCase = true)) {
                                Donvi(id, name, phone, address)
                            } else null
                        }
                    }
                    callback(donviList)
                } catch (e: Exception) {
                    Log.e("DBHelper", "Error in searchDonvi", e)
                    callback(emptyList())
                }
            }
            .addOnFailureListener { e ->
                Log.e("DBHelper", "Error getting documents in searchDonvi", e)
                callback(emptyList())
            }
    }

    fun updateDonvi(donvi: Donvi) {
        firestore.collection(DONVI_COLLECTION).document(donvi.id.toString()).set(donvi)
    }


    fun getDonviById(id: Int, callback: (Donvi) -> Unit) {
        firestore.collection(DONVI_COLLECTION).document(id.toString()).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val donvi = document.toObject(Donvi::class.java)
                    if (donvi != null) {
                        callback(donvi)
                    }
                }
            }
    }

    fun deleteDonvi(id: Int) {
        firestore.collection(DONVI_COLLECTION).document(id.toString()).delete()
    }
    private val USER_COLLECTION = "users"

    fun saveUserRole(userId: String, email: String, role: String, callback: ((Boolean) -> Unit)? = null) {
        val user = hashMapOf(
            "email" to email,
            "role" to role
        )

        firestore.collection(USER_COLLECTION).document(userId).set(user)
            .addOnSuccessListener {
                Log.d("DBHelper", "User role successfully saved for user: $userId")
                callback?.invoke(true)
            }
            .addOnFailureListener { e ->
                Log.e("DBHelper", "Error saving user role", e)
                callback?.invoke(false)
            }
    }

    fun getUserRole(userId: String, callback: (String?) -> Unit) {
        firestore.collection(USER_COLLECTION).document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val role = document.getString("role")
                    Log.d("DBHelper", "Retrieved role for user $userId: $role")
                    callback(role)
                } else {
                    Log.d("DBHelper", "No user document found for ID: $userId")
                    callback(null)
                }
            }
            .addOnFailureListener { e ->
                Log.e("DBHelper", "Error getting user role", e)
                callback(null)
            }
    }

    fun updateUserRole(userId: String, newRole: String, callback: ((Boolean) -> Unit)? = null) {
        firestore.collection(USER_COLLECTION).document(userId)
            .update("role", newRole)
            .addOnSuccessListener {
                Log.d("DBHelper", "User role successfully updated for user: $userId")
                callback?.invoke(true)
            }
            .addOnFailureListener { e ->
                Log.e("DBHelper", "Error updating user role", e)
                callback?.invoke(false)
            }
    }


}