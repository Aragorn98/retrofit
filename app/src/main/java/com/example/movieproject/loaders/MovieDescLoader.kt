package com.example.movieproject.loaders

import android.content.SharedPreferences
import com.example.movieproject.LangPref
import com.example.movieproject.listeners.MovieDescLoadListener
import com.example.movieproject.api.MovieService
import com.example.movieproject.api.models.MovieDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDescLoader(val listener: MovieDescLoadListener) : Callback<MovieDetails> {

    fun loadMovieDesc(id: String) {

        MovieService.movieApi.getMovieDesc(id, LangPref.lang).enqueue(this)
    }

    override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
        listener.onMovieDescLoadError(t)
    }

    override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {
        listener.onMovieDescLoaded(response.body()!!)
    }

}