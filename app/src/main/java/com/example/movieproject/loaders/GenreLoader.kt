package com.example.movieproject.loaders

import android.content.SharedPreferences
import com.example.movieproject.LangPref
import com.example.movieproject.listeners.GenreLoadListener
import com.example.movieproject.api.MovieService
import com.example.movieproject.api.models.Genres
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GenreLoader(val listener: GenreLoadListener) : Callback<Genres> {



    fun loadGenres() {

        MovieService.movieApi.getGenres(LangPref.lang).enqueue(this)
    }

    override fun onFailure(call: Call<Genres>, t: Throwable) {
        listener.onGenresLoadError(t)
    }

    override fun onResponse(call: Call<Genres>, response: Response<Genres>) {
//        listener.onMoviesLoaded(response.body()?.results!!)
        listener.onGenresLoaded(response.body()?.genres!!)
    }

}