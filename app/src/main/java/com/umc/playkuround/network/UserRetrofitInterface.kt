package com.umc.playkuround.network

import com.umc.playkuround.data.*
import retrofit2.Call
import retrofit2.http.*

interface UserRetrofitInterface {

    @POST("/api/users/register")
    fun register(@Body user : User) : Call<UserTokenResponse>

    @GET("/api/users/availability")
    fun isAvailable(@Query("nickname") nickname : String) : Call<DuplicateResponse>

    @POST("/api/users/logout")
    fun logout(@Header("Authorization") token : String) : Call<CommonResponse>

    @GET("/api/users")
    fun getUserInfo(@Header("Authorization") token : String) : Call<UserProfileResponse>

    @GET("/api/users/notification")
    fun getNotification(@Header("Authorization") token : String, @Query("version") version : String) : Call<NotificationResponse>

    @GET("/api/users/game-score")
    fun getGameScores(@Header("Authorization") token : String) : Call<HighestScoresResponse>

    @POST("/api/fake-door")
    fun fakeDoor(@Header("Authorization") token : String) : Call<CommonResponse>

}