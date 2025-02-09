package com.android.grunfeld_project.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.android.grunfeld_project.MainActivity
import com.android.grunfeld_project.R
import com.android.grunfeld_project.network.SupabaseClient.supabaseClient
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        updateUserFcmToken(token)
    }

    fun updateUserFcmToken(token: String) {
        // Launch a coroutine to perform the network call.
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Attempt to load the current session.
                val session = supabaseClient.auth.sessionManager.loadSession()
                // Check if the user is logged in (i.e., session and user ID are not null).
                if (session?.user?.id == null) {
                    Log.d("FirebaseMessagingService", "User not logged in. Not updating token.")
                    return@launch
                }
                val userId = session.user!!.id
                val payload = mapOf("user_id" to userId, "fcm_token" to token)
                val response = supabaseClient.from("user_tokens").insert(payload)
                Log.d("FirebaseMessagingService", "Token updated: $response")
            } catch (e: Exception) {
                Log.e("FirebaseMessagingService", "Error updating token: ${e.message}")
            }
        }
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            sendNotification(it.title, it.body)
        }
    }

    private fun sendNotification(title: String?, messageBody: String?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "class_schedule_channel"
        val defaultSoundUri =
            android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_app) // Use your custom notification icon
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // For Android Oreo and above, create a notification channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Class Schedule Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(Random().nextInt(), notificationBuilder.build())
    }
}