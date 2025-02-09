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

class FirebaseMessagingService : FirebaseMessagingService() ***REMOVED***

    override fun onNewToken(token: String) ***REMOVED***
        super.onNewToken(token)
        updateUserFcmToken(token)
***REMOVED***

    private fun updateUserFcmToken(token: String) ***REMOVED***
        // Launch a coroutine to perform the network call.
        CoroutineScope(Dispatchers.IO).launch ***REMOVED***
            try ***REMOVED***
                val userId = supabaseClient.auth.sessionManager.loadSession()?.user?.id
                val payload = mapOf("user_id" to userId, "fcm_token" to token)
                val response = supabaseClient.from("user_tokens").insert(payload)
                Log.d("FirebaseMessagingService", "Token updated: $response")
    ***REMOVED*** catch (e: Exception) ***REMOVED***
                Log.e("FirebaseMessagingService", "Error updating token: $***REMOVED***e.message***REMOVED***")
    ***REMOVED***
***REMOVED***
***REMOVED***

    override fun onMessageReceived(remoteMessage: RemoteMessage) ***REMOVED***
        remoteMessage.notification?.let ***REMOVED***
            sendNotification(it.title, it.body)
***REMOVED***
***REMOVED***

    private fun sendNotification(title: String?, messageBody: String?) ***REMOVED***
        val intent = Intent(this, MainActivity::class.java).apply ***REMOVED***
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
***REMOVED***
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) ***REMOVED***
            val channel = NotificationChannel(
                channelId,
                "Class Schedule Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
***REMOVED***

        notificationManager.notify(Random().nextInt(), notificationBuilder.build())
***REMOVED***
***REMOVED***