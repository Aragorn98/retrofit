package com.example.movieproject.loaders

import com.example.movieproject.api.MovieService
import com.example.movieproject.api.models.RequestToken
import com.example.movieproject.listeners.RequestTokenLoadListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestTokenLoader(val listener: RequestTokenLoadListener): Callback<RequestToken> {

    fun loadRequestToken() {
        MovieService.movieApi.getRequestToken().enqueue(this)
    }

    override fun onFailure(call: Call<RequestToken>, t: Throwable) {
        listener.onRequestTokenLoadError(t)
    }

    override fun onResponse(call: Call<RequestToken>, response: Response<RequestToken>) {
        listener.onRequestTokenLoaded(response.body()!!)
    }

}