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


class ProfileFragment : Fragment() ***REMOVED***

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? ***REMOVED***

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val bundle = arguments
        lateinit var rollNumber: String
        if (bundle != null) ***REMOVED***
            rollNumber = bundle.getString("roll_number").toString()
***REMOVED***else***REMOVED***
            rollNumber = ""
***REMOVED***

        lateinit var userProfile: Array<Any>
        lifecycleScope.launch ***REMOVED***
            if (rollNumber != "") ***REMOVED***
                userProfile = loadSessionAndCurrentUser(rollNumber)
    ***REMOVED*** else ***REMOVED***
                userProfile = loadSessionAndCurrentUser("")
    ***REMOVED***
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
            githubProfileButton.setOnClickListener ***REMOVED***
                val githubProfileUrl = "https://github.com/$***REMOVED***userProfile[5]***REMOVED***"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(githubProfileUrl))
                startActivity(intent)
    ***REMOVED***
***REMOVED***

        val pexelAPI = BuildConfig.PEXEL_API_KEY
        val bannerImageView = view.findViewById<ImageView>(R.id.bannerImageView)
        lifecycleScope.launch ***REMOVED***
            val randomBannerImageUrl = fetchRandomBannerImageUrl(pexelAPI)
            Log.d("Banner", "Banner URL: $randomBannerImageUrl")
            Glide.with(requireContext())
                .load(randomBannerImageUrl)
                .transition(com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade())
                .into(bannerImageView)
***REMOVED***

        val badgeRecyclerView = view.findViewById<RecyclerView>(R.id.userBadgesRecyclerView)
        lifecycleScope.launch ***REMOVED***
            if (rollNumber != "") ***REMOVED***
                userProfile = loadSessionAndCurrentUser(rollNumber)
    ***REMOVED*** else ***REMOVED***
                userProfile = loadSessionAndCurrentUser("")
    ***REMOVED***
            val userData: PostgrestResult = supabaseClient.from("user_badges").select ***REMOVED***
                filter ***REMOVED***
                    eq("roll_number", userProfile[2])
        ***REMOVED***
    ***REMOVED***
            val badgeList = parseBadgeData(userData.data)
            val adapter = BadgeListAdapter(badgeList)
            badgeRecyclerView.adapter = adapter
            badgeRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)

***REMOVED***


        val editAboutButton = view.findViewById<TextView>(R.id.userAboutEdit)
        if (rollNumber != "") ***REMOVED***
            editAboutButton.visibility = View.GONE
***REMOVED***

        editAboutButton.setOnClickListener ***REMOVED***
            val aboutView = view.findViewById<TextView>(R.id.userAboutView)
            val aboutString = aboutView.text.toString()

            val bundle = Bundle()
            bundle.putString("about", aboutString)
            bundle.putString("roll_number", userProfile[2].toString())
            UpdateAboutDialog(bundle)

***REMOVED***




        return view
***REMOVED***

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun loadSessionAndCurrentUser(rollNumber:String): Array<Any> ***REMOVED***

        val session = supabaseClient.auth.sessionManager.loadSession()
        lateinit var userData: PostgrestResult
        if (rollNumber != "") ***REMOVED***
            userData = supabaseClient.from("users").select ***REMOVED***
                filter ***REMOVED***
                    eq("roll_number", rollNumber)
        ***REMOVED***
    ***REMOVED***

***REMOVED***else ***REMOVED***
            userData = supabaseClient.from("users").select ***REMOVED***
                filter ***REMOVED***
                    session?.user?.id?.let ***REMOVED*** eq("id", it) ***REMOVED***
        ***REMOVED***
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
        val githubUserName = fetchedUser[0].username.trim('"')
        if(fetchedUser[0].about == null || fetchedUser[0].about == "" || fetchedUser[0].about == "NULL")***REMOVED***
            fetchedUser[0].about = "I Love Open Source Software... 💗💗💗"
***REMOVED***
        return arrayOf(githubProfileUrl, nameString, rollNumberString, rank, pointsInt, githubUserName, fetchedUser[0].about)
***REMOVED***

    fun parseUserData(jsonString: String): List<User> ***REMOVED***
        val gson = Gson()
        // Create a TypeToken for a List<User>
        val userListType = object : TypeToken<List<User>>() ***REMOVED******REMOVED***.type
        return gson.fromJson(jsonString, userListType)
***REMOVED***

    suspend fun fetchRandomBannerImageUrl(apiKey: String): String? ***REMOVED***

        val randomPage = Math.floor(Math.random() * 10) + 1
        val client = HttpClient()

        return try ***REMOVED***

            val response: HttpResponse = client.get("https://api.pexels.com/v1/search?query=abstract&orientation=landscape&per_page=1&page=$***REMOVED***randomPage***REMOVED***") ***REMOVED***
                header(HttpHeaders.Authorization, apiKey)
                parameter("page", randomPage)
                parameter("per_page", 1)
    ***REMOVED***

            if (response.status.isSuccess()) ***REMOVED***
                val bodyString = response.bodyAsText()

                val pexelsResponse = Gson().fromJson(bodyString, PexelsResponse::class.java)

                if (pexelsResponse.photos.isNotEmpty()) ***REMOVED***
                    val imageUrl = pexelsResponse.photos.first().src.landscape
                    imageUrl
        ***REMOVED*** else ***REMOVED***
                    null
        ***REMOVED***
    ***REMOVED*** else ***REMOVED***
                null
    ***REMOVED***
***REMOVED*** catch (e: Exception) ***REMOVED***
            e.printStackTrace()
            null
***REMOVED*** finally ***REMOVED***
            client.close()
***REMOVED***
***REMOVED***

    fun parseBadgeData(jsonString: String): List<UserBadge> ***REMOVED***
        val gson = Gson()
        // Create a TypeToken for a List<User>
        val userListType = object : TypeToken<List<UserBadge>>() ***REMOVED******REMOVED***.type
        return gson.fromJson(jsonString, userListType)
***REMOVED***

    fun UpdateAboutDialog(bundle: Bundle)***REMOVED***
        val updateAboutDialogView = layoutInflater.inflate(R.layout.update_about_dialog, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(updateAboutDialogView)
            .create()

        val aboutString = bundle?.getString("about").toString()

        updateAboutDialogView.findViewById<EditText>(R.id.userAboutView).setText(aboutString)

        dialog.setCancelable(false)
        updateAboutDialogView.findViewById<TextView>(R.id.dialog_cancel).setOnClickListener ***REMOVED***
            dialog.dismiss()
***REMOVED***
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        updateAboutDialogView.findViewById<TextView>(R.id.save_about).setOnClickListener ***REMOVED***
            val aboutView = updateAboutDialogView.findViewById<TextView>(R.id.userAboutView)
            val aboutString = aboutView?.text.toString()
            lifecycleScope.launch ***REMOVED***
                val session = supabaseClient.auth.sessionManager.loadSession()
                supabaseClient.from("users").update(
                    mapOf("about" to aboutString)
                ) ***REMOVED***
                    filter ***REMOVED***
                        session?.user?.id?.let ***REMOVED*** eq("id", it) ***REMOVED***
            ***REMOVED***
        ***REMOVED***

                val aboutView = view?.findViewById<TextView>(R.id.userAboutView)
                aboutView?.text = aboutString
                dialog.dismiss()

    ***REMOVED***
***REMOVED***
        dialog.show()
***REMOVED***

***REMOVED***