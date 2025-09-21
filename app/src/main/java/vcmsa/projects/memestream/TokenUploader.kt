package com.vcmsa.projects.memestream

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging

object TokenUploader {

    fun uploadToken() {
        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid ?: "guest"

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("TokenUploader", "Fetching FCM token failed", task.exception)
                    return@addOnCompleteListener
                }

                val token: String? = task.result
                if (token == null) {
                    Log.e("TokenUploader", "❌ Token is null")
                    return@addOnCompleteListener
                }

                Log.d("TokenUploader", "✅ Got FCM Token: $token")

                // ✅ Save to Firestore
                val db: FirebaseFirestore = FirebaseFirestore.getInstance()
                val userDoc = db.collection("users").document(userId)

                val tokenMap: Map<String, Any> = mapOf("fcmToken" to token)

                userDoc.set(tokenMap, SetOptions.merge())
                    .addOnSuccessListener { _: Void? ->
                        Log.d("TokenUploader", "✅ Token uploaded successfully for $userId")
                    }
                    .addOnFailureListener { e: Exception ->
                        Log.e("TokenUploader", "❌ Error uploading token", e)
                    }
            }
    }
}
