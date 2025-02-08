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


class LeaderBoardFragment : Fragment() ***REMOVED***

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? ***REMOVED***
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_leader_board, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.leaderBoardRecyclerView)



        lifecycleScope.launch ***REMOVED***
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

            searchBar.setOnFocusChangeListener ***REMOVED*** _, hasFocus ->
                if (hasFocus) ***REMOVED***
                    searchBar.text?.clear()
        ***REMOVED***
    ***REMOVED***


            searchBar.addTextChangedListener(object : TextWatcher ***REMOVED***
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) ***REMOVED*** ***REMOVED***

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) ***REMOVED***
                    val query = s?.toString() ?: ""
                    // Get the first letter of the query in lowercase, if available
                    val firstLetter = query.firstOrNull()?.lowercaseChar()

                    // Filter the users whose name contains the query text
                    val filteredUsers = users.filter ***REMOVED*** user ->
                        user.name.contains(query, ignoreCase = true)
            ***REMOVED***.sortedWith ***REMOVED*** a, b ->
                        // Check whether the names start with the first letter of the query
                        val aStartsWith = firstLetter?.let ***REMOVED*** a.name.startsWith(it, ignoreCase = true) ***REMOVED*** ?: false
                        val bStartsWith = firstLetter?.let ***REMOVED*** b.name.startsWith(it, ignoreCase = true) ***REMOVED*** ?: false

                        when ***REMOVED***
                            aStartsWith && !bStartsWith -> -1  // a comes first
                            !aStartsWith && bStartsWith -> 1   // b comes first
                            else -> ***REMOVED***
                                // If both have the same status for starting with the letter, sort by points descending
                                val pointsDiff = b.points - a.points
                                if (pointsDiff != 0) ***REMOVED***
                                    pointsDiff
                        ***REMOVED*** else ***REMOVED***
                                    // If points are equal, fallback to alphabetical order
                                    a.name.compareTo(b.name, ignoreCase = true)
                        ***REMOVED***
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***

                    adapter.userList = filteredUsers
                    adapter.notifyDataSetChanged()
        ***REMOVED***

                override fun afterTextChanged(s: Editable?) ***REMOVED*** ***REMOVED***
    ***REMOVED***)


            searchBar.setOnEditorActionListener ***REMOVED*** textView, actionId, keyEvent ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) ***REMOVED***
                    // Hide the soft keyboard
                    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(searchBar.windowToken, 0)
                    // Remove focus from the search bar
                    searchBar.clearFocus()
                    true
        ***REMOVED*** else ***REMOVED***
                    false
        ***REMOVED***
    ***REMOVED***

***REMOVED***
        return view
***REMOVED***

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun loadSessionAndCurrentUser(): Array<Any> ***REMOVED***
        val session = supabaseClient.auth.sessionManager.loadSession()
        val userData: PostgrestResult = supabaseClient.from("users").select ***REMOVED***
            filter ***REMOVED***
                session?.user?.id?.let ***REMOVED*** eq("id", it) ***REMOVED***
    ***REMOVED***
***REMOVED***
        val fetchedUser = parseUserData(userData.data)
        val githubProfileUrl = fetchedUser[0].profile_image.trim('"')
        val nameString = fetchedUser[0].name
        val rollNumberString = fetchedUser[0].roll_number
        val pointsInt = fetchedUser[0].points
        var rank = ""
        if (pointsInt < 200) ***REMOVED***
            rank = "Trainee"
***REMOVED***else if (pointsInt < 500) ***REMOVED***
            rank = "Avenger"
***REMOVED***else if (pointsInt < 1000) ***REMOVED***
            rank = "Captain"
***REMOVED***else if (pointsInt >= 1000) ***REMOVED***
            rank = "Director"
***REMOVED***


        return arrayOf(githubProfileUrl, nameString, rollNumberString, rank, pointsInt)
***REMOVED***

    private suspend fun loadAllUsers(): List<User> ***REMOVED***
        val userData: PostgrestResult = supabaseClient.from("users").select()
        return parseUserData(userData.data).sortedByDescending***REMOVED*** user -> user.points ***REMOVED***
***REMOVED***

    fun parseUserData(jsonString: String): List<User> ***REMOVED***
        val gson = Gson()
        // Create a TypeToken for a List<User>
        val userListType = object : TypeToken<List<User>>() ***REMOVED******REMOVED***.type
        return gson.fromJson(jsonString, userListType)
***REMOVED***
***REMOVED***