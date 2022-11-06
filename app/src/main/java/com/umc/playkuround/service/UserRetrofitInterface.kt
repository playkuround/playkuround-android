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

}