package com.android.grunfeld_project.activities.UserAuth

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.android.grunfeld_project.MainActivity
import com.android.grunfeld_project.R
import com.android.grunfeld_project.models.User
import com.android.grunfeld_project.network.SupabaseClient
import com.android.grunfeld_project.services.FirebaseMessagingService
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.messaging.FirebaseMessaging
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.handleDeeplinks
import io.github.jan.supabase.auth.providers.Github
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import java.time.LocalDateTime
import java.util.ConcurrentModificationException
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
class AuthActivity : AppCompatActivity() ***REMOVED***

    private val supabaseClient = SupabaseClient.supabaseClient

    override fun onCreate(savedInstanceState: Bundle?) ***REMOVED***
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        // Apply window insets for proper layout.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) ***REMOVED*** v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
***REMOVED***
        window.statusBarColor = getColor(R.color.black)

        // Check if this activity was launched as a deep link callback.
        val data: Uri? = intent?.data
        if (data != null && data.scheme == "myapp" && data.host == "callback") ***REMOVED***
            supabaseClient.handleDeeplinks(intent, onSessionSuccess = ***REMOVED*** session ->
                lifecycleScope.launch ***REMOVED***
                    updateUserData(session)
                    markUserAsLoggedIn()
                    navigateToMainActivity()
        ***REMOVED***
    ***REMOVED***)
            return
***REMOVED***

        // Otherwise, display the login UI.
        val nameInput = findViewById<TextInputEditText>(R.id.nameInput)
        val rollNumberInput = findViewById<TextInputEditText>(R.id.rollNumberInput)
        val batchYearInput = findViewById<AutoCompleteTextView>(R.id.batchYearInput)
        val loginButton = findViewById<TextView>(R.id.loginButton)

        // Setup dropdown for academic years.
        val academicYears = listOf("First Year", "Second Year", "Third Year", "Fourth Year")
        val adapter = ArrayAdapter(this, R.layout.custom_dropdown_item, academicYears)
        batchYearInput.setAdapter(adapter)
        batchYearInput.threshold = 1
        batchYearInput.setOnClickListener ***REMOVED*** batchYearInput.showDropDown() ***REMOVED***

        loginButton.setOnClickListener ***REMOVED***
            val name = nameInput.text.toString().trim()
            val rollNumber = rollNumberInput.text.toString().trim()
            val batchYear = batchYearInput.text.toString().trim()

            if (name.isNotEmpty() && rollNumber.isNotEmpty() && batchYear.isNotEmpty()) ***REMOVED***
                if (rollNumber.length != 5) ***REMOVED***
                    rollNumberInput.error = "Roll number must be 5 characters long"
                    showToast("Roll number must be 5 characters long")
                    return@setOnClickListener
        ***REMOVED***

                // Save extra user info in SharedPreferences.
                val prefs = getSharedPreferences("user_extra_info", Context.MODE_PRIVATE)
                prefs.edit().apply ***REMOVED***
                    putString("username", name)
                    putString("rollNumber", rollNumber)
                    putString("academicYear", batchYear)
                    apply()
        ***REMOVED***

                // Initiate GitHub OAuth login.
                lifecycleScope.launch ***REMOVED***
                    loginWithGitHub()
        ***REMOVED***
    ***REMOVED*** else ***REMOVED***
                if (name.isEmpty()) nameInput.error = "Name cannot be empty"
                if (rollNumber.isEmpty()) rollNumberInput.error = "Roll number cannot be empty"
                if (batchYear.isEmpty()) batchYearInput.error = "Batch year cannot be empty"
    ***REMOVED***
***REMOVED***
***REMOVED***

    private suspend fun loginWithGitHub() ***REMOVED***
        try ***REMOVED***
            supabaseClient.auth.signInWith(
                provider = Github,
                redirectUrl = "myapp://callback"
            )
***REMOVED*** catch (e: Exception) ***REMOVED***
            e.printStackTrace()
            showToast("Error during login: $***REMOVED***e.message***REMOVED***")
***REMOVED***
***REMOVED***
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun updateUserData(session: UserSession) ***REMOVED***
        // Retrieve extra user details.
        val prefs = getSharedPreferences("user_extra_info", Context.MODE_PRIVATE)
        val name = prefs.getString("username", "unknown") ?: "unknown"
        val rollNumber = prefs.getString("rollNumber", "unknown") ?: "unknown"
        val academicYear = prefs.getString("academicYear", "unknown") ?: "unknown"

        // Retrieve GitHub metadata.
        val metadata = session.user?.userMetadata
        val githubUsername = metadata?.get("user_name").toString()
        val githubProfileUrl = metadata?.get("avatar_url").toString()

        val loginDate = LocalDateTime.now().toString()

        // Build a JsonObject with proper typed values.
        val newUserJson = mapOf(
            "id" to JsonPrimitive(session.user!!.id),
            "username" to JsonPrimitive(githubUsername), // Adjust as desired: if you want to use name from your form, use name.
            "roll_number" to JsonPrimitive(rollNumber),
            "academic_year" to JsonPrimitive(academicYear),
            "name" to JsonPrimitive(name),
            "points" to JsonPrimitive(0),
            "profile_image" to JsonPrimitive(githubProfileUrl),
            "createdat" to JsonPrimitive(loginDate),
            "lastlogin" to JsonPrimitive(loginDate)
        )

        val userPrefs = getSharedPreferences("user_data", MODE_PRIVATE)
        val editor = userPrefs.edit()
        editor.putString("username", githubUsername)
        editor.putString("rollNumber", rollNumber)
        editor.putString("academicYear", academicYear)
        editor.putString("id", session.user!!.id)
        editor.putString("name", name)
        editor.apply()

        try ***REMOVED***
            val existingUser = supabaseClient.from("users").select ***REMOVED***
                    filter ***REMOVED*** eq("id", session.user!!.id) ***REMOVED***
        ***REMOVED***
            if (existingUser.data.length == 2) ***REMOVED***
                supabaseClient.from("users").insert(newUserJson) ***REMOVED******REMOVED***
    ***REMOVED***

***REMOVED*** catch (e: Exception) ***REMOVED***
            Log.e("updateUserData", "Error inserting user data: $***REMOVED***e.message***REMOVED***")
            e.printStackTrace()
***REMOVED***
***REMOVED***

    private fun markUserAsLoggedIn() ***REMOVED***
        val loginPrefs = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
        loginPrefs.edit().putBoolean("isLoggedIn", true).apply()
***REMOVED***

    private fun navigateToMainActivity() ***REMOVED***
        val intent = Intent(this, MainActivity::class.java).apply ***REMOVED***
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
***REMOVED***
        startActivity(intent)
        finish()
***REMOVED***

    private fun showToast(message: String) ***REMOVED***
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
***REMOVED***

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean ***REMOVED***
        if (ev.action == MotionEvent.ACTION_DOWN) ***REMOVED***
            // Get the view that currently has focus
            val v = currentFocus
            if (v is EditText) ***REMOVED***
                // Create a rectangle outRect of the focused view
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                // If the touch event is outside the focused view, clear focus and hide keyboard
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) ***REMOVED***
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
        ***REMOVED***
    ***REMOVED***
***REMOVED***
        return super.dispatchTouchEvent(ev)
***REMOVED***

***REMOVED***
