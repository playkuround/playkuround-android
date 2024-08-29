package com.umc.playkuround.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface NoticeRetrofitInterface {

    @GET("/api/events")
    fun getNotice(@Header("Authorization") token : String) : Call<NoticeResponse>

}