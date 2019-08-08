package com.example.movieproject.listeners

import com.example.movieproject.api.models.Video

interface VideoLoadListener {

    fun onVideosLoaded(video: Video)
    fun onVideosLoadError(throwable: Throwable)
}