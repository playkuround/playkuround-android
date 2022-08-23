package com.umc.playkuround.service

import com.umc.playkuround.data.User
import com.umc.playkuround.data.UserTokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserRetrofitInterface {

    @POST("/api/users/register")
    fun register(@Body user : User) : Call<UserTokenResponse>

}