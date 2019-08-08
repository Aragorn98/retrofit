package com.example.movieproject.listeners
import com.example.movieproject.api.models.Result

interface ByGenreLoadListener {

    fun onByGenreLoaded(movies: Result)
    fun onByGenreLoadError(throwable: Throwable)
}