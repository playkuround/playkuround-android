package com.umc.playkuround.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AttendanceRetrofitInterface {

    @GET("/api/attendances")
    fun getAttendanceData(@Header("Authorization") token : String) : Call<AttendanceResponse>

    @POST("/api/attendances")
    fun attendanceToday(@Header("Authorization") token : String, @Body location : UserLocation) : Call<GetBadgeResponse>

}