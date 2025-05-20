package com.example.hbomax.services

import com.example.hbomax.BuildConfig
import com.example.hbomax.ui.MovieResponse
import com.example.hbomax.ui.VideoResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular") // Endpoint for popular movies
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY, // API key from BuildConfig
        @Query("page") page: Int = 1
    ): MovieResponse // Retrofit will handle suspend functions and parse the response

    @GET("movie/{movie_id}/videos") // Endpoint for movie videos
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int, // Movie ID to fetch videos for
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY, // API key from BuildConfig
    ): VideoResponse // Retrofit will handle suspend functions and parse the response

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("query") searchQuery: String,
        @Query("page") page: Int = 1
        // You can add other params like include_adult, year, etc.
    ): MovieResponse // Reusing MovieResponse as the structure is often similar
}


object TMDBApiServiceProvider {
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    val imageBaseUrl = "https://image.tmdb.org/t/p/w500" // For constructing full image URLs

    // Configure Json for lenient parsing (optional but good for APIs you don't control)
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true // If an Int is expected but String "null" is received, it converts to null/default
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ApiService::class.java)
    }
}