package com.android.grunfeld_project.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.grunfeld_project.R

class DateSelectionListAdapter(private val dateList: List<String>): RecyclerView.Adapter<DateSelectionListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayView = itemView.findViewById<TextView>(R.id.dayView)
        val dateView = itemView.findViewById<TextView>(R.id.dateView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DateSelectionListAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_date_selection, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DateSelectionListAdapter.ViewHolder, position: Int) {
        val date = dateList[position]
        holder.dayView.text = date.substring(0, 3)
        holder.dateView.text = date.substring(4)
    }

    override fun getItemCount(): Int {
        return dateList.size
    }
}