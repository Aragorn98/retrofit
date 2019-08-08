package com.example.movieproject.listeners

import com.example.movieproject.api.models.MovieDetails

interface MovieDescLoadListener {

    fun onMovieDescLoaded(movies: MovieDetails)
    fun onMovieDescLoadError(throwable: Throwable)
}