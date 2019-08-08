package com.example.movieproject.listeners

import com.example.movieproject.api.models.Genre
import com.example.movieproject.api.models.Genres

interface GenreLoadListener {

    fun onGenresLoaded(genres: List<Genre>)
    fun onGenresLoadError(throwable: Throwable)
}