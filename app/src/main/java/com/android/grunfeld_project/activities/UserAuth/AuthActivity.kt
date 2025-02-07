package com.android.grunfeld_project.activities.UserAuth

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.grunfeld_project.R
import com.android.grunfeld_project.network.SupabaseClient
import io.github.jan.supabase.auth.auth

class AuthActivity : AppCompatActivity() ***REMOVED***

    private val supabaseClient = SupabaseClient.supabaseClient

    override fun onCreate(savedInstanceState: Bundle?) ***REMOVED***
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
//        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) ***REMOVED*** v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
***REMOVED***
        window.statusBarColor = getColor(R.color.black)
***REMOVED***
***REMOVED***