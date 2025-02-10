package com.android.grunfeld_project.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.decode.SvgDecoder
import coil.load
import com.android.grunfeld_project.R
import com.android.grunfeld_project.models.UserBadge
import com.bumptech.glide.Glide
import io.ktor.client.content.LocalFileContent

class BadgeListAdapter(badgeList: List<UserBadge>): RecyclerView.Adapter<BadgeListAdapter.ViewHolder>() ***REMOVED***
    private val badgeList = badgeList

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)***REMOVED***
        val badgeImage = itemView.findViewById<ImageView>(R.id.badgeView)
***REMOVED***

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgeListAdapter.ViewHolder ***REMOVED***
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_badge, parent, false)
        return ViewHolder(view)
***REMOVED***

    override fun onBindViewHolder(holder: BadgeListAdapter.ViewHolder, position: Int) ***REMOVED***
        val badge = badgeList[position]
        Log.d("Badge", badge.allocated_url)
        holder.badgeImage.load(badge.allocated_url) ***REMOVED***
            crossfade(true)
            decoderFactory(SvgDecoder.Factory())
***REMOVED***
***REMOVED***

    override fun getItemCount(): Int ***REMOVED***
        return badgeList.size
***REMOVED***

***REMOVED***