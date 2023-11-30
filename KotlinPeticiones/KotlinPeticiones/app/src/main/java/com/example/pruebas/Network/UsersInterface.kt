package com.example.pruebas.Network

import com.example.pruebas.Models.ResultUser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UsersInterface {
    @GET("/api")
    fun getUser(
        @Query("results") results: Int? = null
    ): Call<ResultUser>
}