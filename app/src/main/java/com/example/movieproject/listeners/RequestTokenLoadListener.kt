package com.example.movieproject.listeners

import com.example.movieproject.api.models.RequestToken

interface RequestTokenLoadListener {
    fun onRequestTokenLoaded(requestToken: RequestToken)
    fun onRequestTokenLoadError(throwable: Throwable)
}