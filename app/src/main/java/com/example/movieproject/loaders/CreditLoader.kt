package com.example.movieproject.loaders


import android.content.SharedPreferences
import com.example.movieproject.LangPref
import com.example.movieproject.listeners.CreditLoadListener
import com.example.movieproject.api.MovieService
import com.example.movieproject.api.models.Credits
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreditLoader(val listener: CreditLoadListener) : Callback<Credits> {

    fun loadTopCredits(id: String) {
        MovieService.movieApi.getTopCredits(id, LangPref.lang).enqueue(this)
    }

    override fun onFailure(call: Call<Credits>, t: Throwable) {
        listener.onCreditsLoadError(t)
    }

    override fun onResponse(call: Call<Credits>, response: Response<Credits>) {
        listener.onCreditsLoaded(response.body()!!)
    }

}