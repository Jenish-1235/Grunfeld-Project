package com.android.grunfeld_project.fragments

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.grunfeld_project.BuildConfig
import com.android.grunfeld_project.R
import com.android.grunfeld_project.adapters.BadgeListAdapter
import com.android.grunfeld_project.models.PexelsResponse
import com.android.grunfeld_project.models.User
import com.android.grunfeld_project.models.UserBadge
import com.android.grunfeld_project.network.SupabaseClient.supabaseClient
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.launch
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*


class ProfileFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val bundle = arguments
        lateinit var rollNumber: String
        if (bundle != null) {
            rollNumber = bundle.getString("roll_number").toString()
        }else{
            rollNumber = ""
        }

        lateinit var userProfile: Array<Any>
        lifecycleScope.launch {
            if (rollNumber != "") {
                userProfile = loadSessionAndCurrentUser(rollNumber)
            } else {
                userProfile = loadSessionAndCurrentUser("")
            }
            val nameView = view.findViewById<TextView>(R.id.nameView)
            val rankView = view.findViewById<TextView>(R.id.rankView)
            val pointsView = view.findViewById<TextView>(R.id.pointsView)
            nameView.text = userProfile[1].toString()
            rankView.text = userProfile[3].toString()
            pointsView.text = userProfile[4].toString() + " Points"
            val aboutView = view.findViewById<TextView>(R.id.userAboutView)
            aboutView.text = userProfile[6].toString()

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

        val pexelAPI = BuildConfig.PEXEL_API_KEY
        val bannerImageView = view.findViewById<ImageView>(R.id.bannerImageView)
        lifecycleScope.launch {
            val randomBannerImageUrl = fetchRandomBannerImageUrl(pexelAPI)
            Log.d("Banner", "Banner URL: $randomBannerImageUrl")
            Glide.with(requireContext())
                .load(randomBannerImageUrl)
                .transition(com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade())
                .into(bannerImageView)
        }

        val badgeRecyclerView = view.findViewById<RecyclerView>(R.id.userBadgesRecyclerView)
        lifecycleScope.launch {
            if (rollNumber != "") {
                userProfile = loadSessionAndCurrentUser(rollNumber)
            } else {
                userProfile = loadSessionAndCurrentUser("")
            }
            val userData: PostgrestResult = supabaseClient.from("user_badges").select {
                filter {
                    eq("roll_number", userProfile[2])
                }
            }
            val badgeList = parseBadgeData(userData.data)
            val adapter = BadgeListAdapter(badgeList)
            badgeRecyclerView.adapter = adapter
            badgeRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)

        }


        val editAboutButton = view.findViewById<TextView>(R.id.userAboutEdit)
        if (rollNumber != "") {
            editAboutButton.visibility = View.GONE
        }

        editAboutButton.setOnClickListener {
            val aboutView = view.findViewById<TextView>(R.id.userAboutView)
            val aboutString = aboutView.text.toString()

            val bundle = Bundle()
            bundle.putString("about", aboutString)
            bundle.putString("roll_number", userProfile[2].toString())
            UpdateAboutDialog(bundle)

        }




        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun loadSessionAndCurrentUser(rollNumber:String): Array<Any> {

        val session = supabaseClient.auth.sessionManager.loadSession()
        lateinit var userData: PostgrestResult
        if (rollNumber != "") {
            userData = supabaseClient.from("users").select {
                filter {
                    eq("roll_number", rollNumber)
                }
            }

        }else {
            userData = supabaseClient.from("users").select {
                filter {
                    session?.user?.id?.let { eq("id", it) }
                }
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
        if(fetchedUser[0].about == null || fetchedUser[0].about == "" || fetchedUser[0].about == "NULL"){
            fetchedUser[0].about = "I Love Open Source Software... 💗💗💗"
        }
        return arrayOf(githubProfileUrl, nameString, rollNumberString, rank, pointsInt, githubUserName, fetchedUser[0].about)
    }

    fun parseUserData(jsonString: String): List<User> {
        val gson = Gson()
        // Create a TypeToken for a List<User>
        val userListType = object : TypeToken<List<User>>() {}.type
        return gson.fromJson(jsonString, userListType)
    }

    suspend fun fetchRandomBannerImageUrl(apiKey: String): String? {

        val randomPage = Math.floor(Math.random() * 10) + 1
        val client = HttpClient()

        return try {

            val response: HttpResponse = client.get("https://api.pexels.com/v1/search?query=abstract&orientation=landscape&per_page=1&page=${randomPage}") {
                header(HttpHeaders.Authorization, apiKey)
                parameter("page", randomPage)
                parameter("per_page", 1)
            }

            if (response.status.isSuccess()) {
                val bodyString = response.bodyAsText()

                val pexelsResponse = Gson().fromJson(bodyString, PexelsResponse::class.java)

                if (pexelsResponse.photos.isNotEmpty()) {
                    val imageUrl = pexelsResponse.photos.first().src.landscape
                    imageUrl
                } else {
                    null
                }
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            client.close()
        }
    }

    fun parseBadgeData(jsonString: String): List<UserBadge> {
        val gson = Gson()
        // Create a TypeToken for a List<User>
        val userListType = object : TypeToken<List<UserBadge>>() {}.type
        return gson.fromJson(jsonString, userListType)
    }

    fun UpdateAboutDialog(bundle: Bundle){
        val updateAboutDialogView = layoutInflater.inflate(R.layout.update_about_dialog, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(updateAboutDialogView)
            .create()

        val aboutString = bundle?.getString("about").toString()

        updateAboutDialogView.findViewById<EditText>(R.id.userAboutView).setText(aboutString)

        dialog.setCancelable(false)
        updateAboutDialogView.findViewById<TextView>(R.id.dialog_cancel).setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        updateAboutDialogView.findViewById<TextView>(R.id.save_about).setOnClickListener {
            val aboutView = updateAboutDialogView.findViewById<TextView>(R.id.userAboutView)
            val aboutString = aboutView?.text.toString()
            lifecycleScope.launch {
                val session = supabaseClient.auth.sessionManager.loadSession()
                supabaseClient.from("users").update(
                    mapOf("about" to aboutString)
                ) {
                    filter {
                        session?.user?.id?.let { eq("id", it) }
                    }
                }

                val aboutView = view?.findViewById<TextView>(R.id.userAboutView)
                aboutView?.text = aboutString
                dialog.dismiss()

            }
        }
        dialog.show()
    }

}