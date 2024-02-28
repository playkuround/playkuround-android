package com.umc.playkuround.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface BadgeRetrofitInterface {

    @GET("/api/badges")
    fun getUserBadges(@Header("Authorization") token : String) : Call<UserBadgeResponse>

    @POST("/api/badges/manual")
    fun manuallyAddBadge(@Body body : ManualBadgeData) : Call<BadgeResultResponse>

    @POST("/api/badges/dream-of-duck")
    fun addDreamOfDuckBadge(@Header("Authorization") token : String) : Call<BadgeResultResponse>

}