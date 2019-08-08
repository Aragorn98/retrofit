package com.example.movieproject.loaders

import android.util.Log
import com.example.movieproject.api.MovieService
import com.example.movieproject.api.models.CreateSession
import com.example.movieproject.api.models.RequestToken
import com.example.movieproject.api.models.RequestTokenClass
import com.example.movieproject.listeners.SessionListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateSession(val listener: SessionListener, val requestToken: RequestTokenClass): Callback<CreateSession> {

    fun createSession() {
        MovieService.movieApi.createSession(requestToken).enqueue(this)
    }

    override fun onFailure(call: Call<CreateSession>, t: Throwable) {
        listener.onSessionCreateError(t)
    }

    override fun onResponse(call: Call<CreateSession>, response: Response<CreateSession>) {
        Log.d("argyn", response.toString())
        if (response.isSuccessful) {
            listener.onSessionCreated(response.body()!!)
        } else {
            response.errorBody()?.string()
        }
    }

}