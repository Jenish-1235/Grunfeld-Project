package com.android.grunfeld_project.fragments

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.android.grunfeld_project.R
import com.android.grunfeld_project.models.User
import com.android.grunfeld_project.network.SupabaseClient.supabaseClient
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        lifecycleScope.launch {
            val userProfile = loadSessionAndCurrentUser()
            val nameView = view.findViewById<TextView>(R.id.nameView)
            val rankView = view.findViewById<TextView>(R.id.rankView)
            val pointsView = view.findViewById<TextView>(R.id.pointsView)
            nameView.text = userProfile[1].toString()
            rankView.text = userProfile[3].toString()
            pointsView.text = userProfile[4].toString() + " Points"

            val profileImage = view.findViewById<ImageView>(R.id.profileImage)
            Glide.with(requireContext())
                .load(userProfile[0])
                .circleCrop()
                .transition(com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade())
                .into(profileImage)

            val githubProfileButton = view.findViewById<ImageView>(R.id.userGithub)
            githubProfileButton.setOnClickListener {
                val githubProfileUrl = "https://github.com/${userProfile[5]}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(githubProfileUrl))
                startActivity(intent)
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
        val githubUserName = fetchedUser[0].username.trim('"')


        return arrayOf(githubProfileUrl, nameString, rollNumberString, rank, pointsInt, githubUserName)
    }

    fun parseUserData(jsonString: String): List<User> {
        val gson = Gson()
        // Create a TypeToken for a List<User>
        val userListType = object : TypeToken<List<User>>() {}.type
        return gson.fromJson(jsonString, userListType)
    }

}