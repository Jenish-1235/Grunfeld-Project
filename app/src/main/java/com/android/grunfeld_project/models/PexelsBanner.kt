package com.android.grunfeld_project.models

data class PexelsResponse(
    val photos: List<Photo>
)

data class Photo(
    val src: PhotoSrc
)

data class PhotoSrc(
    val original: String,
    val large: String,
    val medium: String,
    val small: String,
    val portrait: String,
    val landscape: String,
    val tiny: String
)
