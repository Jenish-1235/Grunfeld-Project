package com.android.grunfeld_project.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.grunfeld_project.R
import com.android.grunfeld_project.models.Event

class EventListAdapter(private val eventList: List<Event>) : RecyclerView.Adapter<EventListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val eventTitleView = itemView.findViewById<TextView>(R.id.eventTitleView)
        val instructorView = itemView.findViewById<TextView>(R.id.instructorView)
        val timeView = itemView.findViewById<TextView>(R.id.timeView)
        val locationView = itemView.findViewById<TextView>(R.id.locationView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_schedule, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = eventList[position]
        holder.eventTitleView.text = event.title
        holder.instructorView.text = event.instructor
        holder.timeView.text = event.time
        holder.locationView.text = event.location
    }
}