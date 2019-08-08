package com.example.movieproject.listeners

import com.example.movieproject.api.models.Credits

interface CreditLoadListener {

    fun onCreditsLoaded(credits: Credits)
    fun onCreditsLoadError(throwable: Throwable)
}