package com.android.grunfeld_project.network

import android.util.Log
import com.android.grunfeld_project.network.SupabaseClient.supabaseClient
import com.google.firebase.messaging.FirebaseMessaging
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private fun updateUserFCM() ***REMOVED***
    // Fetch the current FCM token.
    FirebaseMessaging.getInstance().token.addOnCompleteListener ***REMOVED*** task ->
        if (!task.isSuccessful) ***REMOVED***
            Log.w("MainActivity", "Fetching FCM registration token failed", task.exception)
            return@addOnCompleteListener
***REMOVED***
        val token = task.result
        Log.d("MainActivity", "Fetched FCM token: $token")
        // Launch a coroutine to perform network operations.
        CoroutineScope(Dispatchers.IO).launch ***REMOVED***
            try ***REMOVED***
                // Load the current Supabase session.
                val session = supabaseClient.auth.sessionManager.loadSession()
                // Only update the token if a valid user session exists.
                if (session?.user?.id == null) ***REMOVED***
                    Log.d("MainActivity", "No user logged in. Skipping token update.")
                    return@launch
        ***REMOVED***
                val userId = session.user!!.id
                // Prepare payload to update the "user_tokens" table.
                val payload = mapOf("user_id" to userId, "fcm_token" to token)
                // Insert the token into your database.
                val response = supabaseClient.from("user_tokens").insert(payload)
                Log.d("MainActivity", "Token update response: $response")
    ***REMOVED*** catch (e: Exception) ***REMOVED***
                Log.e("MainActivity", "Error updating user token: $***REMOVED***e.message***REMOVED***")
    ***REMOVED***
***REMOVED***
***REMOVED***
***REMOVED***
