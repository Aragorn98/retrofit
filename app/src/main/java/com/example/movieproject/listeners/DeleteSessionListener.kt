package com.example.movieproject.listeners

import com.example.movieproject.api.models.DeleteSession

interface DeleteSessionListener {
    fun onSessionDeleted(response: DeleteSession)
    fun onSessionDeleteError(throwable: Throwable)
}