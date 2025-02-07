package com.android.grunfeld_project

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.android.grunfeld_project.activities.UserAuth.AuthActivity
import com.android.grunfeld_project.network.SupabaseClient.supabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val loginPrefs = getSharedPreferences("loginPrefs", MODE_PRIVATE)
        val isLoggedIn = loginPrefs.getBoolean("isLoggedIn", false)
        if (!isLoggedIn) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        } else {
            createTopBar()
            bottomNavBar()
        }
    }

    fun createTopBar() {
        // Your implementation for top bar.
    }

    fun bottomNavBar() {
        // Your implementation for bottom navigation bar.
    }
}
