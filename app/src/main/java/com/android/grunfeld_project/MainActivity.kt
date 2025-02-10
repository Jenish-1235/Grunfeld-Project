package com.android.grunfeld_project

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.android.grunfeld_project.activities.UserAuth.AuthActivity
import com.android.grunfeld_project.fragments.DevPostsFragment
import com.android.grunfeld_project.fragments.LeaderBoardFragment
import com.android.grunfeld_project.fragments.ProfileFragment
import com.android.grunfeld_project.fragments.ScheduleFragment
import com.android.grunfeld_project.network.SupabaseClient.supabaseClient
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.firebase.messaging.FirebaseMessaging
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive


class MainActivity : AppCompatActivity() ***REMOVED***
    companion object ***REMOVED***
        const val PREFS_NAME = "notificationPrefs"
        const val KEY_WENT_TO_SETTINGS = "wentToSettings"
***REMOVED***

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) ***REMOVED***
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) ***REMOVED*** v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
***REMOVED***

        window.statusBarColor = Color.BLACK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) ***REMOVED***
            val decor = window.decorView
            var flags = decor.systemUiVisibility
            flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            decor.systemUiVisibility = flags
***REMOVED***
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false


        val loginPrefs = getSharedPreferences("loginPrefs", MODE_PRIVATE)
        val isLoggedIn = loginPrefs.getBoolean("isLoggedIn", false)
        val isNotificationDenied = getSharedPreferences("isNotificationDenied", MODE_PRIVATE)

        if (!isLoggedIn) ***REMOVED***
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
***REMOVED***else***REMOVED***

            if(!NotificationManagerCompat.from(this).areNotificationsEnabled() && !isNotificationDenied.getBoolean("isDenied", false))***REMOVED***
                showNotificationDialog()
                lifecycleScope.launch ***REMOVED***
                    val githubProfile = sessionReloadAndUpdateProfile()
                    bottomNavBar(githubProfile)
                    updateTokenAfterLogin()
        ***REMOVED***
    ***REMOVED***else ***REMOVED***
                lifecycleScope.launch ***REMOVED***
                    val githubProfile = sessionReloadAndUpdateProfile()
                    bottomNavBar(githubProfile)
                    updateTokenAfterLogin()
        ***REMOVED***
    ***REMOVED***
***REMOVED***
***REMOVED***

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun sessionReloadAndUpdateProfile():String***REMOVED***
        val session = supabaseClient.auth.sessionManager.loadSession()
        val user = session?.user?.userMetadata
        val rawGithubProfile: JsonElement? = user?.get("avatar_url")  // expected to be a JsonElement

        // Log the type for debugging
        Log.d("profile type", rawGithubProfile!!::class.java.typeName)

        // Safely cast to JsonPrimitive and extract its content
        val githubProfileUrl = (rawGithubProfile as? JsonPrimitive)?.content ?: ""
        Log.d("profile url", "Extracted URL: $githubProfileUrl")

        return githubProfileUrl
***REMOVED***

    fun bottomNavBar(githubProfile: String) ***REMOVED***
        // Your implementation for bottom navigation bar.
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE)
        tabLayout.setTabTextColors(Color.WHITE, Color.WHITE)
        tabLayout.setSelectedTabIndicatorHeight(0)
        tabLayout.removeAllTabs()

        val fragmentContainerView = findViewById<FrameLayout>(R.id.fragment_container_view)

        var leaderBoardTab = tabLayout.newTab()
        val leaderBoardTabView: View = layoutInflater.inflate(R.layout.navigation_tablayout_item, null)
        leaderBoardTab.customView = leaderBoardTabView
        val leaderBoardTabIcon = leaderBoardTabView.findViewById<ImageView>(R.id.tab_icon)
        leaderBoardTabIcon.setImageResource(R.drawable.leaderboard)
        leaderBoardTabIcon.imageTintList = getColorStateList(R.color.gray)
        tabLayout.addTab(leaderBoardTab)

        var scheduleTab = tabLayout.newTab()
        val scheduleTabView: View = layoutInflater.inflate(R.layout.navigation_tablayout_item, null)
        scheduleTab.customView = scheduleTabView
        val scheduleTabIcon = scheduleTabView.findViewById<ImageView>(R.id.tab_icon)
        scheduleTabIcon.setImageResource(R.drawable.schedule)
        scheduleTabIcon.imageTintList = getColorStateList(R.color.gray)
        tabLayout.addTab(scheduleTab)

        var devPostsTab = tabLayout.newTab()
        val devPostsTabView: View = layoutInflater.inflate(R.layout.navigation_tablayout_item, null)
        devPostsTab.customView = devPostsTabView
        val devPostsTabIcon = devPostsTabView.findViewById<ImageView>(R.id.tab_icon)
        devPostsTabIcon.setImageResource(R.drawable.posts)
        devPostsTabIcon.imageTintList = getColorStateList(R.color.gray)
        tabLayout.addTab(devPostsTab)

        val profileTab = tabLayout.newTab()
        val profileTabView: View = layoutInflater.inflate(R.layout.navigation_tablayout_item, null)
        profileTab.customView = profileTabView
        var profileIcon = profileTabView.findViewById<ImageView>(R.id.tab_icon)
        Glide.with(this)
            .load(githubProfile)
            .circleCrop()
            .into(profileIcon)
        tabLayout.addTab(profileTab)

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener ***REMOVED***
            @SuppressLint("ResourceAsColor")
            override fun onTabSelected(tab: TabLayout.Tab?) ***REMOVED***
                when (tab?.position) ***REMOVED***
                    0 -> ***REMOVED***
                        val leaderBoardFragment = LeaderBoardFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_view, leaderBoardFragment)
                            .commit()

                        leaderBoardTabIcon.imageTintList = getColorStateList(R.color.blue)
                        scheduleTabIcon.imageTintList = getColorStateList(R.color.gray)
                        devPostsTabIcon.imageTintList = getColorStateList(R.color.gray)
            ***REMOVED***
                    1 -> ***REMOVED***
                        val scheduleFragment = ScheduleFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_view, scheduleFragment)
                            .commit()

                        leaderBoardTabIcon.imageTintList = getColorStateList(R.color.gray)
                        scheduleTabIcon.imageTintList = getColorStateList(R.color.blue)
                        devPostsTabIcon.imageTintList = getColorStateList(R.color.gray)
            ***REMOVED***
                    2 -> ***REMOVED***
                        val devPostsFragment = DevPostsFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_view, devPostsFragment)
                            .commit()

                        leaderBoardTabIcon.imageTintList = getColorStateList(R.color.gray)
                        scheduleTabIcon.imageTintList = getColorStateList(R.color.gray)
                        devPostsTabIcon.imageTintList = getColorStateList(R.color.blue)
            ***REMOVED***
                    3 -> ***REMOVED***

                        val bundle = Bundle()
                        bundle.putString("roll_number", "")
                        val profileFragment = ProfileFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_view, profileFragment)
                            .commit()

                        leaderBoardTabIcon.imageTintList = getColorStateList(R.color.gray)
                        scheduleTabIcon.imageTintList = getColorStateList(R.color.gray)
                        devPostsTabIcon.imageTintList = getColorStateList(R.color.gray)

            ***REMOVED***
        ***REMOVED***
    ***REMOVED***

            override fun onTabUnselected(tab: TabLayout.Tab?) ***REMOVED***
    ***REMOVED***

            override fun onTabReselected(tab: TabLayout.Tab?) ***REMOVED***
                when (tab?.position) ***REMOVED***
                    0 -> ***REMOVED***
                        val leaderBoardFragment = LeaderBoardFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_view, leaderBoardFragment)
                            .commit()

                        leaderBoardTabIcon.imageTintList = getColorStateList(R.color.blue)
                        scheduleTabIcon.imageTintList = getColorStateList(R.color.gray)
                        devPostsTabIcon.imageTintList = getColorStateList(R.color.gray)
            ***REMOVED***
                    1 -> ***REMOVED***
                        val scheduleFragment = ScheduleFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_view, scheduleFragment)
                            .commit()

                        leaderBoardTabIcon.imageTintList = getColorStateList(R.color.gray)
                        scheduleTabIcon.imageTintList = getColorStateList(R.color.blue)
                        devPostsTabIcon.imageTintList = getColorStateList(R.color.gray)
            ***REMOVED***
                    2 -> ***REMOVED***
                        val devPostsFragment = DevPostsFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_view, devPostsFragment)
                            .commit()

                        leaderBoardTabIcon.imageTintList = getColorStateList(R.color.gray)
                        scheduleTabIcon.imageTintList = getColorStateList(R.color.gray)
                        devPostsTabIcon.imageTintList = getColorStateList(R.color.blue)
            ***REMOVED***
                    3 -> ***REMOVED***
                        val profileFragment = ProfileFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_view, profileFragment)
                            .commit()

                        leaderBoardTabIcon.imageTintList = getColorStateList(R.color.gray)
                        scheduleTabIcon.imageTintList = getColorStateList(R.color.gray)
                        devPostsTabIcon.imageTintList = getColorStateList(R.color.gray)

            ***REMOVED***
        ***REMOVED***
    ***REMOVED***
***REMOVED***)
        scheduleTab.select()

***REMOVED***

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() ***REMOVED***
        super.onResume()

        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (prefs.getBoolean(KEY_WENT_TO_SETTINGS, false)) ***REMOVED***

            val notificationsEnabled = NotificationManagerCompat.from(this).areNotificationsEnabled()
            if (notificationsEnabled) ***REMOVED***
                prefs.edit().remove(KEY_WENT_TO_SETTINGS).apply()
                lifecycleScope.launch ***REMOVED***
                    val githubProfile = sessionReloadAndUpdateProfile()
                    bottomNavBar(githubProfile)
                    updateTokenAfterLogin()
        ***REMOVED***
    ***REMOVED***
***REMOVED***
***REMOVED***

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotificationDialog()***REMOVED***
        val dialogView = layoutInflater.inflate(R.layout.notification_permission_dialog, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.setCancelable(false)
        dialogView.findViewById<Button>(R.id.dialog_cancel).setOnClickListener ***REMOVED***
            dialog.dismiss()
            var isNotificationDenied = getSharedPreferences("isNotificationDenied", MODE_PRIVATE)
            isNotificationDenied.edit().putBoolean("isDenied", true).apply()
***REMOVED***

        dialogView.findViewById<Button>(R.id.dialog_settings).setOnClickListener ***REMOVED***
            val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit().putBoolean(KEY_WENT_TO_SETTINGS, true).apply()

            // Redirect to notification settings.
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            startActivity(intent)
            dialog.dismiss()
***REMOVED***
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()
***REMOVED***

    private fun updateTokenAfterLogin() ***REMOVED***
        FirebaseMessaging.getInstance().token.addOnCompleteListener ***REMOVED*** task ->
            if (!task.isSuccessful) ***REMOVED***
                Log.w("AuthActivity", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
    ***REMOVED***
            val token = task.result
            updateUserFCM(token)
***REMOVED***
***REMOVED***

    private fun updateUserFCM(token: String) ***REMOVED***
        CoroutineScope(Dispatchers.IO).launch ***REMOVED***
            try ***REMOVED***
                val session = supabaseClient.auth.sessionManager.loadSession()
                if (session?.user?.id == null) ***REMOVED***
                    Log.d("MainActivity", "No user logged in. Skipping token update.")
                    return@launch
        ***REMOVED***
                val userId = session.user!!.id
                val payload = mapOf("user_id" to userId, "fcm_token" to token)

                val selectResponse = supabaseClient.from("user_tokens").select***REMOVED***
                    filter ***REMOVED***
                        eq("user_id", userId)
            ***REMOVED***
        ***REMOVED***

                if (selectResponse.data != null && selectResponse.data.toString().isNotEmpty() && selectResponse.data.toString() != "[]") ***REMOVED***
                    supabaseClient.from("user_tokens").update(payload) ***REMOVED***
                        filter ***REMOVED***
                            eq("user_id", userId)
                ***REMOVED***
            ***REMOVED***
        ***REMOVED*** else ***REMOVED***
                   supabaseClient.from("user_tokens").insert(payload)

        ***REMOVED***
    ***REMOVED*** catch (e: Exception) ***REMOVED***
                Log.e("MainActivity", "Error updating user token: $***REMOVED***e.message***REMOVED***")
    ***REMOVED***
***REMOVED***
***REMOVED***

***REMOVED***
