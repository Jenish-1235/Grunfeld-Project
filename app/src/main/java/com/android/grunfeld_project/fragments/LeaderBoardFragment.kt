package com.android.grunfeld_project.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.grunfeld_project.R
import com.android.grunfeld_project.adapters.LeaderBoardListAdapter
import com.android.grunfeld_project.models.User
import com.android.grunfeld_project.network.SupabaseClient.supabaseClient
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.launch


class LeaderBoardFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_leader_board, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.leaderBoardRecyclerView)



        lifecycleScope.launch {
            val userProfile = loadSessionAndCurrentUser()
            val leaderBoardProfile = view.findViewById<ImageView>(R.id.leaderBoardProfileImage)
            val nameView = view.findViewById<TextView>(R.id.displayNameView)
            val rollNumberView = view.findViewById<TextView>(R.id.rollNumberView)
            val rankView = view.findViewById<TextView>(R.id.rankView)
            val pointsView = view.findViewById<TextView>(R.id.pointsView)

            Glide.with(requireContext())
                .load(userProfile[0])
                .circleCrop()
                .into(leaderBoardProfile)

            nameView.text = userProfile[1].toString()
            rollNumberView.text = userProfile[2].toString()
            rankView.text = userProfile[3].toString()
            pointsView.text = userProfile[4].toString() + " Points"

            val users = loadAllUsers()
            val adapter = LeaderBoardListAdapter(users)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            val searchBar = view.findViewById<TextInputEditText>(R.id.searchBar)

            searchBar.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    searchBar.text?.clear()
                }
            }


            searchBar.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val query = s?.toString() ?: ""
                    // Get the first letter of the query in lowercase, if available
                    val firstLetter = query.firstOrNull()?.lowercaseChar()

                    // Filter the users whose name contains the query text
                    val filteredUsers = users.filter { user ->
                        user.name.contains(query, ignoreCase = true)
                    }.sortedWith { a, b ->
                        // Check whether the names start with the first letter of the query
                        val aStartsWith = firstLetter?.let { a.name.startsWith(it, ignoreCase = true) } ?: false
                        val bStartsWith = firstLetter?.let { b.name.startsWith(it, ignoreCase = true) } ?: false

                        when {
                            aStartsWith && !bStartsWith -> -1  // a comes first
                            !aStartsWith && bStartsWith -> 1   // b comes first
                            else -> {
                                // If both have the same status for starting with the letter, sort by points descending
                                val pointsDiff = b.points - a.points
                                if (pointsDiff != 0) {
                                    pointsDiff
                                } else {
                                    // If points are equal, fallback to alphabetical order
                                    a.name.compareTo(b.name, ignoreCase = true)
                                }
                            }
                        }
                    }

                    adapter.userList = filteredUsers
                    adapter.notifyDataSetChanged()
                }

                override fun afterTextChanged(s: Editable?) { }
            })


            searchBar.setOnEditorActionListener { textView, actionId, keyEvent ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Hide the soft keyboard
                    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(searchBar.windowToken, 0)
                    // Remove focus from the search bar
                    searchBar.clearFocus()
                    true
                } else {
                    false
                }
            }

        }
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun loadSessionAndCurrentUser(): Array<Any> {
        val session = supabaseClient.auth.sessionManager.loadSession()
        val userData: PostgrestResult = supabaseClient.from("users").select {
            filter {
                session?.user?.id?.let { eq("id", it) }
            }
        }
        val fetchedUser = parseUserData(userData.data)
        val githubProfileUrl = fetchedUser[0].profile_image.trim('"')
        val nameString = fetchedUser[0].name
        val rollNumberString = fetchedUser[0].roll_number
        val pointsInt = fetchedUser[0].points
        var rank = ""
        if (pointsInt < 200) {
            rank = "Trainee"
        }else if (pointsInt < 500) {
            rank = "Avenger"
        }else if (pointsInt < 1000) {
            rank = "Captain"
        }else if (pointsInt >= 1000) {
            rank = "Director"
        }


        return arrayOf(githubProfileUrl, nameString, rollNumberString, rank, pointsInt)
    }

    private suspend fun loadAllUsers(): List<User> {
        val userData: PostgrestResult = supabaseClient.from("users").select()
        return parseUserData(userData.data).sortedByDescending{ user -> user.points }
    }

    fun parseUserData(jsonString: String): List<User> {
        val gson = Gson()
        // Create a TypeToken for a List<User>
        val userListType = object : TypeToken<List<User>>() {}.type
        return gson.fromJson(jsonString, userListType)
    }
}