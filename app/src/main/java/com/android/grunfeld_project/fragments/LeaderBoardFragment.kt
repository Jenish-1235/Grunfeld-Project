package com.android.grunfeld_project.fragments

import android.media.Image
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.grunfeld_project.R
import com.android.grunfeld_project.adapters.LeaderBoardListAdapter
import com.android.grunfeld_project.models.User
import com.android.grunfeld_project.network.SupabaseClient.supabaseClient
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive


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
        if (pointsInt != 0) ***REMOVED***
            if (pointsInt < 200) ***REMOVED***
                rank = "Trainee"
    ***REMOVED***
            if (pointsInt < 500) ***REMOVED***
                rank = "Avenger"
    ***REMOVED***
            if (pointsInt < 700) ***REMOVED***
                rank = "Captain"
        ***REMOVED***
            if (pointsInt < 1000) ***REMOVED***
                rank = "Legend"
    ***REMOVED***
            if (pointsInt >= 1000) ***REMOVED***
                rank = "Director"
    ***REMOVED***
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