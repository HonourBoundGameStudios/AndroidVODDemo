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
    val overview: String,
    @SerialName("release_date")
    val releaseDate: String
)

@Serializable
data class PopularMovieResponse(
    val page: Int,
    val results: List<Movie>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)

@Serializable
data class Video(
    @SerialName("iso_639_1")
    val iso6391: String, // Language code

    @SerialName("iso_3166_1")
    val iso3166_1: String, // Country code

    @SerialName("name")
    val name: String, // Name of the video

    @SerialName("key")
    val videoKey: String, // Key for the video

    @SerialName("site")
    val site: String, // Site where the video is hosted (e.g., YouTube)

    @SerialName("size")
    val size: Int, // Size of the video (resolution)

    @SerialName("type")
    val type: String, // Type of video (e.g., Trailer, Clip)

    @SerialName("official")
    val official: Boolean, // Whether the video is official or not

    @SerialName("published_at")
    val publishedAt: String, // When the video was published

    val id: String
)

@Serializable
data class VideoResponse(
    val id: Int,
    @SerialName("results")
    val results: List<Video> // List of videos for the movie
)