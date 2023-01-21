package com.umc.playkuround.service

import com.umc.playkuround.data.*
import retrofit2.Call
import retrofit2.http.*

interface UserRetrofitInterface {

    @POST("/api/users/register")
    fun register(@Body user : User) : Call<UserTokenResponse>

    @GET("/api/users/duplication")
    fun isDuplicate(@Query("nickname") nickname : String) : Call<DuplicateResponse>

    @POST("/api/users/login")
    fun login(@Header("Authorization") token : String) : Call<UserTokenResponse>

    @POST("/api/users/logout")
    fun logout(@Header("Authorization") token : String) : Call<LogoutResponse>

    @POST("/api/auth/tokens")
    fun reissuanceToken(@Header("Authorization") token : String) : Call<RefreshTokenResponse>

    @POST("/api/auth/emails")
    fun sendEmail(@Body target : String) : Call<EmailResponse>

    @GET("/api/auth/emails")
    fun certifyCode(@Query("email") email : String, @Query("code") code : String) : Call<EmailCertifyResponse>

    @POST("/api/attendances")
    fun attendanceToday(@Header("Authorization") token : String, @Body location : Location) : Call<CommonResponse>

    @GET("/api/attendances")
    fun getAttendanceDates(@Header("Authorization") token : String) : Call<CommonResponse>

    @GET("/api/landmarks")
    fun getNearLandmark(@Header("Authorization") token : String, @Query("latitude") latitude : String, @Query("longitude") longitude : String) : Call<CommonResponse>

    @GET("/api/scores/rankings")
    fun getUserRanking(@Header("Authorization") token : String) : Call<CommonResponse>

    @GET("/api/scores/rankings/top100")
    fun getTop100Ranking(@Header("Authorization") token : String) : Call<CommonResponse>

    @POST("/api/scores")
    fun updateUserScore(@Header("Authorization") token : String, @Body scoreType : String) : Call<CommonResponse>

    @POST("/api/adventures")
    fun saveAdventureLog(@Header("Authorization") token : String, @Body landmark : LandMark) : Call<CommonResponse>

    @GET("/api/adventures")
    fun getUserAdventureLog(@Header("Authorization") token : String) : Call<CommonResponse>

}