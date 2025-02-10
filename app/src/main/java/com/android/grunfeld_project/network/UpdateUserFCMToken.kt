package com.android.grunfeld_project.network

import android.util.Log
import com.android.grunfeld_project.network.SupabaseClient.supabaseClient
import com.google.firebase.messaging.FirebaseMessaging
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private fun updateUserFCM() {
    // Fetch the current FCM token.
    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.w("MainActivity", "Fetching FCM registration token failed", task.exception)
            return@addOnCompleteListener
        }
        val token = task.result
        Log.d("MainActivity", "Fetched FCM token: $token")
        // Launch a coroutine to perform network operations.
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Load the current Supabase session.
                val session = supabaseClient.auth.sessionManager.loadSession()
                // Only update the token if a valid user session exists.
                if (session?.user?.id == null) {
                    Log.d("MainActivity", "No user logged in. Skipping token update.")
                    return@launch
                }
                val userId = session.user!!.id
                // Prepare payload to update the "user_tokens" table.
                val payload = mapOf("user_id" to userId, "fcm_token" to token)
                // Insert the token into your database.
                val response = supabaseClient.from("user_tokens").insert(payload)
                Log.d("MainActivity", "Token update response: $response")
            } catch (e: Exception) {
                Log.e("MainActivity", "Error updating user token: ${e.message}")
            }
        }
    }
}
