package com.example.movieproject.api

import com.example.movieproject.api.models.*
import retrofit2.Call
import retrofit2.http.*

interface MovieApi {
    @GET("movie/popular?api_key=71eb7fc9baa53fdc5920a373c657c65e&page=1")
    fun getPopularMovies(@Query("language") lang: String): Call<Result>

    @GET("movie/top_rated?api_key=71eb7fc9baa53fdc5920a373c657c65e&page=1")
    fun getTopRatedMovies(@Query("language") lang: String): Call<Result>

    @GET("movie/upcoming?api_key=71eb7fc9baa53fdc5920a373c657c65e&page=1")
    fun getUpcomingMovies(@Query("language") lang: String): Call<Result>

//    @GET("movie/latest?api_key=71eb7fc9baa53fdc5920a373c657c65e&language=en-US&page=1")
//    fun getLatestMovies(): Call<Result>

    @GET("movie/now_playing?api_key=71eb7fc9baa53fdc5920a373c657c65e&page=1")
    fun getNowPlayingMovies(@Query("language") lang: String): Call<Result>

    @GET("movie/{movie_id}/recommendations?api_key=71eb7fc9baa53fdc5920a373c657c65e&")
    fun getRecommendedMovies(
        @Path("movie_id") id: String,
        @Query("language") lang: String): Call<Result>

    @GET("movie/{movie_id}/similar?api_key=71eb7fc9baa53fdc5920a373c657c65e&page=1")
    fun getSimilarMovies(
        @Path("movie_id") id: String,
        @Query("language") lang: String): Call<Result>

    @GET("movie/{movie_id}/videos?api_key=71eb7fc9baa53fdc5920a373c657c65e&")
    fun getVideos(
        @Path("movie_id") id: String,
        @Query("language") lang: String): Call<Video>

    @GET("movie/{movie_id}?api_key=71eb7fc9baa53fdc5920a373c657c65e&")
    fun getMovieDesc(
        @Path("movie_id") id: String,
        @Query("language") lang: String): Call<MovieDetails>

    @GET("discover/movie?api_key=71eb7fc9baa53fdc5920a373c657c65e&")
    fun getMoviesByGenre(
        @Query("with_genres") id: String,
        @Query("language") lang: String): Call<Result>

    @GET("movie/{movie_id}/credits?api_key=71eb7fc9baa53fdc5920a373c657c65e")
    fun getTopCredits(
        @Path("movie_id") id: String,
        @Query("language") lang: String): Call<Credits>

    @GET("genre/movie/list?api_key=71eb7fc9baa53fdc5920a373c657c65e&")
    fun getGenres(@Query("language") lang: String): Call<Genres>

    @GET("authentication/token/new?api_key=71eb7fc9baa53fdc5920a373c657c65e")
    fun getRequestToken(): Call<RequestToken>

    @POST("authentication/session/new?api_key=71eb7fc9baa53fdc5920a373c657c65e")
    fun createSession(@Body requestToken: RequestTokenClass): Call<CreateSession>

    @DELETE("authentication/session?api_key=71eb7fc9baa53fdc5920a373c657c65e")
    fun deleteSession(@Body sessionId: SessionIdClass): Call<DeleteSession>
//
//    @HTTP(method = "DELETE", path = "authentication/session?api_key=71eb7fc9baa53fdc5920a373c657c65e", hasBody = true)
//    fun deleteSession(@Body sessionId: SessionIdClass): Call<DeleteSession>
}