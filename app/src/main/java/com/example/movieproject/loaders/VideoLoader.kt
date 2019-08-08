package com.example.movieproject.loaders

import android.content.SharedPreferences
import com.example.movieproject.LangPref
import com.example.movieproject.api.models.Video


import com.example.movieproject.listeners.VideoLoadListener
import com.example.movieproject.api.MovieService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoLoader(val listener: VideoLoadListener) : Callback<Video> {

    fun loadVideos(id: String) {
        MovieService.movieApi.getVideos(id, LangPref.lang).enqueue(this)
    }

    override fun onFailure(call: Call<Video>, t: Throwable) {
        listener.onVideosLoadError(t)
    }

    override fun onResponse(call: Call<Video>, response: Response<Video>) {
        listener.onVideosLoaded(response.body()!!)
    }

}