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
        if (pointsInt != 0) {
            if (pointsInt < 200) {
                rank = "Trainee"
            }
            if (pointsInt < 500) {
                rank = "Avenger"
            }
            if (pointsInt < 700) {
                rank = "Captain"
                }
            if (pointsInt < 1000) {
                rank = "Legend"
            }
            if (pointsInt >= 1000) {
                rank = "Director"
            }
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