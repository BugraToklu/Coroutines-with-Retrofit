package com.example.androidcoroutines

import com.example.androidcoroutines.api.Cat
import retrofit2.Call
import retrofit2.http.GET

interface API {
    @GET("/facts/random")
    fun getCat(): Call<Cat>
}