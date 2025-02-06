package com.android.grunfeld_project

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.grunfeld_project.activities.UserAuth.AuthActivity

class MainActivity : AppCompatActivity() ***REMOVED***
    override fun onCreate(savedInstanceState: Bundle?) ***REMOVED***
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) ***REMOVED*** v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
***REMOVED***

        val loginPrefs = getSharedPreferences("loginPrefs", MODE_PRIVATE)
        val isLoggedIn = loginPrefs.getBoolean("isLoggedIn", false)
        if (!isLoggedIn) ***REMOVED***
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
***REMOVED***else***REMOVED***
            createTopBar()
            bottomNavBar()
***REMOVED***
***REMOVED***

    // create topbar and bottom navigation bar...
    fun createTopBar()***REMOVED***

***REMOVED***

    fun bottomNavBar()***REMOVED***

***REMOVED***

***REMOVED***