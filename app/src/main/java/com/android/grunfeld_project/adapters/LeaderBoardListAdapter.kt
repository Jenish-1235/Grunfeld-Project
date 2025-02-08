package com.android.grunfeld_project.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.grunfeld_project.R
import com.android.grunfeld_project.models.User
import com.bumptech.glide.Glide

class LeaderBoardListAdapter(userList: List<User>): RecyclerView.Adapter<LeaderBoardListAdapter.LeaderBoardViewHolder>()***REMOVED***
    var userList = userList
    class LeaderBoardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)***REMOVED***
        val profileImage = itemView.findViewById<ImageView>(R.id.leaderBoardProfileImage)
        val displayName = itemView.findViewById<TextView>(R.id.displayNameView)
        val rollNumberView = itemView.findViewById<TextView>(R.id.rollNumberView)
        val githubProfileButton = itemView.findViewById<ImageButton>(R.id.githubProfileButton)
        val rankView = itemView.findViewById<TextView>(R.id.rankView)
        val pointsView = itemView.findViewById<TextView>(R.id.pointsView)
***REMOVED***


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LeaderBoardListAdapter.LeaderBoardViewHolder ***REMOVED***
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_leaderboard, parent, false)
        return LeaderBoardViewHolder(itemView)
***REMOVED***

    override fun onBindViewHolder(
        holder: LeaderBoardListAdapter.LeaderBoardViewHolder,
        position: Int
    ) ***REMOVED***
        val currentUser = userList[position]

        var rank = ""

        if(currentUser.points < 200)***REMOVED***
            rank = "Trainee"
***REMOVED***else if (currentUser.points < 500)***REMOVED***
            rank = "Avenger"
***REMOVED***else if(currentUser.points < 700)***REMOVED***
            rank = "Captain"
***REMOVED***else if (currentUser.points < 1000)***REMOVED***
            rank = "Legend"
***REMOVED***else if (currentUser.points >= 1000)***REMOVED***
            rank = "Director"
***REMOVED***

        Glide.with(holder.itemView.context)
            .load(currentUser.profile_image.trim('"'))
            .circleCrop()
            .into(holder.profileImage)
        holder.displayName.text = currentUser.name
        holder.rollNumberView.text = currentUser.roll_number
        holder.rankView.text = rank
        holder.pointsView.text = currentUser.points.toString()
        holder.githubProfileButton.setOnClickListener ***REMOVED***
            // Handle GitHub profile button click here
            val githubProfileUrl = "https://github.com/$***REMOVED***currentUser.username.trim('"')***REMOVED***"
            // Open the GitHub profile in a web browser
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(githubProfileUrl))
            startActivity(holder.itemView.context, intent, null)
***REMOVED***

***REMOVED***

    override fun getItemCount(): Int ***REMOVED***
        return userList.size
***REMOVED***
***REMOVED***