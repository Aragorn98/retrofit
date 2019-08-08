package com.example.movieproject.listeners

import com.example.movieproject.api.models.Movie
import com.example.movieproject.api.models.Result

interface MovieLoadListener {

    fun onMoviesLoaded(movies: Result)
    fun onMoviesLoadError(throwable: Throwable)
}