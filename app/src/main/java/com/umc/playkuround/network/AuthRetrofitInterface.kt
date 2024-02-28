package com.umc.playkuround.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthRetrofitInterface {

    @POST("/api/auth/emails")
    fun sendEmail(@Body target : String) : Call<EmailResponse>

    @GET("/api/auth/emails")
    fun certifyCode(@Query("email") email : String, @Query("code") code : String) : Call<CertifyCodeResponse>

    @POST("/api/auth/reissue")
    fun reissue(@Body token : ReissueTokens) : Call<UserTokenResponse>

}