package com.android.grunfeld_project.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.grunfeld_project.R
import com.android.grunfeld_project.models.Event

class EventListAdapter(private val eventList: List<Event>) : RecyclerView.Adapter<EventListAdapter.ViewHolder>() ***REMOVED***
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)***REMOVED***
        val eventTitleView = itemView.findViewById<TextView>(R.id.eventTitleView)
        val instructorView = itemView.findViewById<TextView>(R.id.instructorView)
        val timeView = itemView.findViewById<TextView>(R.id.timeView)
        val locationView = itemView.findViewById<TextView>(R.id.locationView)
***REMOVED***

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder ***REMOVED***
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_schedule, parent, false)
        return ViewHolder(itemView)
***REMOVED***

    override fun getItemCount(): Int ***REMOVED***
        return eventList.size
***REMOVED***

    override fun onBindViewHolder(holder: ViewHolder, position: Int) ***REMOVED***
        val event = eventList[position]
        holder.eventTitleView.text = event.title
        holder.instructorView.text = event.instructor
        holder.timeView.text = event.time
        holder.locationView.text = event.location
***REMOVED***
***REMOVED***