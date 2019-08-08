package com.example.movieproject.loaders

import android.util.Log
import com.example.movieproject.api.MovieService
import com.example.movieproject.api.models.DeleteSession
import com.example.movieproject.api.models.SessionIdClass
import com.example.movieproject.listeners.DeleteSessionListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteSession(val listener: DeleteSessionListener, val sessionId: SessionIdClass): Callback<DeleteSession> {

    fun deleteSession() {
//        MovieService.movieApi.deleteSession(sessionId).enqueue(this)
        MovieService.movieApi.deleteSession(sessionId)
    }

    override fun onFailure(call: Call<DeleteSession>, t: Throwable) {
        listener.onSessionDeleteError(t)
    }

    override fun onResponse(call: Call<DeleteSession>, response: Response<DeleteSession>) {
        Log.d("argynDelete", response.toString())
        Log.d("argynDelete", response.body().toString())
        if (response.isSuccessful) {
            listener.onSessionDeleted(response.body()!!)
        } else {
            Log.d("argynDelete", response.errorBody()?.string())
        }
    }

}