package com.example.hbomax // Adjust package name

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Int,
    val title: String,
    @SerialName("poster_path") // API uses snake_case, Kotlin prefers camelCase
    val posterPath: String?, // Poster path can sometimes be null
    @SerialName("overview")
    val overview: String
)

@Serializable
data class MovieResponse(
    val page: Int,
    val results: List<Movie>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)