package com.android.grunfeld_project.fragments

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.grunfeld_project.R
import com.android.grunfeld_project.adapters.DateSelectionListAdapter
import com.android.grunfeld_project.adapters.EventListAdapter
import com.android.grunfeld_project.models.Event
import com.android.grunfeld_project.network.SupabaseClient.supabaseClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class ScheduleFragment : Fragment() ***REMOVED***

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? ***REMOVED***
        val view = inflater.inflate(R.layout.fragment_schedule, container, false)
        val dateSelectionRecyclerView = view.findViewById<RecyclerView>(R.id.dateSelectionRecyclerView)
        val eventRecyclerView = view.findViewById<RecyclerView>(R.id.scheduleRecyclerView)

        lifecycleScope.launch ***REMOVED***
            val events = loadSchedule()

            // Define your date formatters (input & output)
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            // Get today's date and the date 6 days later (i.e. a week from today)
            val today = LocalDate.now()
            val weekLater = today.plusDays(6)

            // Collect unique event dates (as LocalDate) that fall between today and weekLater
            val upcomingEventDates = events.mapNotNull ***REMOVED*** event ->
                try ***REMOVED***
                    val eventDate = LocalDate.parse(event.schedule_date, inputFormatter)
                    if (!eventDate.isBefore(today) && !eventDate.isAfter(weekLater)) eventDate else null
        ***REMOVED*** catch (e: Exception) ***REMOVED***
                    null
        ***REMOVED***
    ***REMOVED***.distinct().sorted()

            // Prepare lists for the date selection RecyclerView:
            // - displayDateList: formatted as "DAYDDth" (e.g., "Mon03rd")
            // - actualDateList: corresponding "yyyy-MM-dd" strings for filtering.
            val displayDateList = mutableListOf<String>()
            val actualDateList = mutableListOf<String>()

            for (date in upcomingEventDates) ***REMOVED***
                val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                val dayOfMonth = date.dayOfMonth
                val dayPadded = String.format("%02d", dayOfMonth)
                val suffix = if (dayOfMonth in 11..13) ***REMOVED***
                    "th"
        ***REMOVED*** else ***REMOVED***
                    when (dayOfMonth % 10) ***REMOVED***
                        1 -> "st"
                        2 -> "nd"
                        3 -> "rd"
                        else -> "th"
            ***REMOVED***
        ***REMOVED***
                // Create the formatted display string (e.g., "Mon03rd")
                displayDateList.add("$dayOfWeek$dayPadded$suffix")
                // Save the actual date string (e.g., "2025-02-03")
                actualDateList.add(date.format(outputFormatter))
    ***REMOVED***

            // Set up the date selection RecyclerView only if there are dates to show.
            if (displayDateList.isEmpty()) ***REMOVED***
                val noClassesMessage = view.findViewById<TextView>(R.id.noClassesMessage)
                noClassesMessage.visibility = View.VISIBLE
    ***REMOVED*** else ***REMOVED***

                val dateSelectionAdapter = DateSelectionListAdapter(displayDateList)
                dateSelectionAdapter.onDateSelected = ***REMOVED*** position ->
                    val selectedDate = actualDateList[position]
                    val filteredEvents = events.filter ***REMOVED*** event ->
                        event.schedule_date == selectedDate
            ***REMOVED***
                    if (filteredEvents.isEmpty()) ***REMOVED***

            ***REMOVED***
                    eventRecyclerView.adapter = EventListAdapter(filteredEvents)
        ***REMOVED***
                dateSelectionRecyclerView.adapter = dateSelectionAdapter
                dateSelectionRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    ***REMOVED***

            // --- Initial Filtering ---
            val todayFormatted = today.format(outputFormatter)
            val initialFilteredEvents = events.filter ***REMOVED*** event ->
                event.schedule_date == todayFormatted
    ***REMOVED***
            if (initialFilteredEvents.isEmpty()) ***REMOVED***
                // show the most recent upcoming event
                val mostRecentEvent = events.minByOrNull ***REMOVED*** event ->
                    LocalDate.parse(event.schedule_date, inputFormatter)
        ***REMOVED***
                if (mostRecentEvent != null) ***REMOVED***
                    eventRecyclerView.adapter = EventListAdapter(listOf(mostRecentEvent))
                    eventRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        ***REMOVED***
    ***REMOVED***else ***REMOVED***
                eventRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                eventRecyclerView.adapter = EventListAdapter(initialFilteredEvents)
    ***REMOVED***
***REMOVED***

        val pastClassesButton = view.findViewById<TextView>(R.id.pastClassesButton)
        pastClassesButton.setOnClickListener ***REMOVED***
            val youtube = "https://www.youtube.com/@grunfeldproject"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtube))
            startActivity(intent)
***REMOVED***
        return view
***REMOVED***

    private suspend fun loadSchedule(): List<Event> ***REMOVED***
        val eventData: PostgrestResult = supabaseClient.from("class_schedule").select()
        return parseEventData(eventData.data)
***REMOVED***

    private fun parseEventData(jsonString: String): List<Event> ***REMOVED***
        val gson = Gson()
        val eventListType = object : TypeToken<List<Event>>() ***REMOVED******REMOVED***.type
        return gson.fromJson(jsonString, eventListType)
***REMOVED***
***REMOVED***
