package com.umc.playkuround.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LandmarkRetrofitInterface {

    @GET("/api/landmarks")
    fun findLandmark(@Header("Authorization") token : String,
                     @Query("latitude") latitude : String,
                     @Query("longitude") longitude : String) : Call<LandmarkResponse>

    @GET("/api/landmarks/{landmarkId}/highest")
    fun findTopPlayer(@Header("Authorization") token : String,
                      @Path("landmarkId") landmarkId : Int) : Call<LandmarkTopResponse>

    @POST("/api/adventures")
    fun sendScore(@Header("Authorization") token : String,
                  @Body adventureData : AdventureData) : Call<GetBadgeResponse>

}