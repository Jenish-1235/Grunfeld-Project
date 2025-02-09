package com.android.grunfeld_project.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.android.grunfeld_project.R
import com.android.grunfeld_project.models.UserBadge
import com.bumptech.glide.Glide

class BadgeListAdapter(badgeList: List<UserBadge>): RecyclerView.Adapter<BadgeListAdapter.ViewHolder>() {
    private val badgeList = badgeList

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val badgeImage = itemView.findViewById<ImageView>(R.id.badgeView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgeListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_badge, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BadgeListAdapter.ViewHolder, position: Int) {
        val badge = badgeList[position]
        Glide.with(holder.itemView.context)
            .load(badge.allocated_url)
            .into(holder.badgeImage)
    }

    override fun getItemCount(): Int {
        return badgeList.size
    }

}