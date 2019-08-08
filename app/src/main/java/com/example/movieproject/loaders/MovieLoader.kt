package com.example.movieproject.loaders

import android.content.SharedPreferences
import com.example.movieproject.LangPref
import com.example.movieproject.listeners.MovieLoadListener
import com.example.movieproject.api.MovieService
import com.example.movieproject.api.models.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieLoader(val listener: MovieLoadListener) : Callback<Result> {
    var moviesListType: String = "ee"

    fun loadPopularMovies() {
        moviesListType = "Popular"
        MovieService.movieApi.getPopularMovies(LangPref.lang).enqueue(this)
    }

    fun loadTopRatedMovies() {
        moviesListType = "Top Rated"
        MovieService.movieApi.getTopRatedMovies(LangPref.lang).enqueue(this)
    }

    fun loadUpcomingMovies() {
        moviesListType = "Upcoming"
        MovieService.movieApi.getUpcomingMovies(LangPref.lang).enqueue(this)
    }

    fun loadNowPlayingMovies() {
        moviesListType = "Now Playing"
        MovieService.movieApi.getNowPlayingMovies(LangPref.lang).enqueue(this)
    }

    fun loadRecommendedMovies(id: String) {
        moviesListType = "Recommended Movies"
        MovieService.movieApi.getRecommendedMovies(id, LangPref.lang).enqueue(this)
    }

    fun loadSimilarMovies(id: String) {
        moviesListType = "Similar movies"
        MovieService.movieApi.getSimilarMovies(id,LangPref.lang).enqueue(this)
    }

    override fun onFailure(call: Call<Result>, t: Throwable) {
        listener.onMoviesLoadError(t)
    }

    override fun onResponse(call: Call<Result>, response: Response<Result>) {
//        listener.onMoviesLoaded(response.body()?.results!!)
        response.body()?.type = moviesListType
        listener.onMoviesLoaded(response.body()!!)
    }

}