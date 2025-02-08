package com.android.grunfeld_project.models

data class Event(
    val id: Int,
    val class_name: String,
    val instructor: String,
    val time: String,
    val room: String,
    val schedule_date: String,
    val created_at: String
)