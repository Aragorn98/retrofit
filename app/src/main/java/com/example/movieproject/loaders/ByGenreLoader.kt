package com.example.movieproject.loaders


import android.content.SharedPreferences
import android.util.Log
import com.example.movieproject.LangPref
import com.example.movieproject.listeners.ByGenreLoadListener
import com.example.movieproject.api.MovieService
import com.example.movieproject.api.models.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ByGenreLoader(val listener: ByGenreLoadListener) : Callback<Result> {
    var moviesListType: String = "ee"


    fun loadMoviesByGenre(id: String, name: String) {
        moviesListType = name
        MovieService.movieApi.getMoviesByGenre(id, LangPref.lang).enqueue(this)
    }

    override fun onFailure(call: Call<Result>, t: Throwable) {
        listener.onByGenreLoadError(t)
    }

    override fun onResponse(call: Call<Result>, response: Response<Result>) {
//        listener.onMoviesLoaded(response.body()?.results!!)
        response.body()?.type = moviesListType
        listener.onByGenreLoaded(response.body()!!)
    }

}