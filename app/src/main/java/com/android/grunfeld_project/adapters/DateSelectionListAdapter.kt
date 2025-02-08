package com.android.grunfeld_project.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.grunfeld_project.R

class DateSelectionListAdapter(private val dateList: List<String>) : RecyclerView.Adapter<DateSelectionListAdapter.ViewHolder>() {

    // Default selected position is 0 so that the first card appears blue on launch.
    var selectedPosition: Int = 0

    // Callback invoked when a date is selected.
    var onDateSelected: ((Int) -> Unit)? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayView: TextView = itemView.findViewById(R.id.dayView)
        val dateView: TextView = itemView.findViewById(R.id.dateView)
        // Assuming your list_item_date_selection layout has a CardView with id "cardView".
        val cardView: CardView = itemView.findViewById(R.id.dateSelectionCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_date_selection, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date = dateList[position]
        // Assuming date format "DAYDDth" (e.g., "Mon03rd")
        holder.dayView.text = date.substring(0, 3)
        holder.dateView.text = date.substring(3)

        // Set background color based on whether this item is selected.
        if (position == selectedPosition) {
            holder.cardView.setCardBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.blue)
            )
        } else {
            holder.cardView.setCardBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, android.R.color.transparent)
            )
        }

        // When a card is clicked, update the selected position and notify changes.
        holder.itemView.setOnClickListener {
            val previousSelected = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousSelected)
            notifyItemChanged(selectedPosition)
            onDateSelected?.invoke(position)
        }
    }

    override fun getItemCount(): Int = dateList.size
}
