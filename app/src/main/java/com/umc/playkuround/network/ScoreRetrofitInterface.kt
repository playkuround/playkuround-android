package com.umc.playkuround.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ScoreRetrofitInterface {

    @GET("/api/scores/rank")
    fun getTop100(@Header("Authorization") token : String) : Call<Top100Response>

    @GET("/api/scores/rank/{landmarkId}")
    fun getLandmarkTop100(@Header("Authorization") token : String, @Path("landmarkId") landmarkId : Int) : Call<Top100Response>

}