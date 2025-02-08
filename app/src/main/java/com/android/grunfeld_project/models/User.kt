package com.android.grunfeld_project.models

data class User(
    val id: String,              // UUID as a String
    val academic_year: String,   // e.g., "First Year"
    val createdAt: String,       // e.g., "2025-02-08T01:56:48.148631"
    val lastLogin: String,       // e.g., "2025-02-08T01:56:48.148631"
    val name: String,            // Full name, e.g., "Ram Tichkule"
    val points: Int = 0,         // Default points, e.g., 70 (if provided, else default 0)
    val profile_image: String,   // URL to the profile image
    val roll_number: String,     // e.g., "10140"
    val username: String         // e.g., "Raam751" (or GitHub username)
)