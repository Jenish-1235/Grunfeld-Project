package com.android.grunfeld_project.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.grunfeld_project.R

class DateSelectionListAdapter(private val dateList: List<String>) : RecyclerView.Adapter<DateSelectionListAdapter.ViewHolder>() ***REMOVED***

    // Callback to be invoked when a date item is selected.
    var onDateSelected: ((Int) -> Unit)? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) ***REMOVED***
        val dayView: TextView = itemView.findViewById(R.id.dayView)
        val dateView: TextView = itemView.findViewById(R.id.dateView)
***REMOVED***

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder ***REMOVED***
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_date_selection, parent, false)
        return ViewHolder(itemView)
***REMOVED***

    override fun onBindViewHolder(holder: ViewHolder, position: Int) ***REMOVED***
        val date = dateList[position]
        // Split the string into day-of-week (first three characters) and the rest (padded day + suffix).
        holder.dayView.text = date.substring(0, 3)
        holder.dateView.text = date.substring(3)

        // Set up the click listener to notify the selected position.
        holder.itemView.setOnClickListener ***REMOVED***
            onDateSelected?.invoke(position)
***REMOVED***
***REMOVED***

    override fun getItemCount(): Int = dateList.size
***REMOVED***
