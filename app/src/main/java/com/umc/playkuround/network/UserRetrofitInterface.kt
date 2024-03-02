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
    fun getNotification(@Header("Authorization") token : String) : Call<NotificationResponse>

    @GET("/api/users/game-score")
    fun getGameScores(@Header("Authorization") token : String) : Call<HighestScoresResponse>

    @GET("/api/fake-door")
    fun fakeDoor(@Header("Authorization") token : String) : Call<CommonResponse>


    // -----------------------------------------------------------------------------------------------------------------


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

    @GET("/api/adventures/{landmarkId}/most")
    fun getPlaceRank(@Header("Authorization") token : String, @Path("landmarkId") landmarkId : Int) : Call<CommonResponse>

    @POST("/api/badges")
    fun saveBadge(@Header("Authorization") token : String, @Body badgeType : String) : Call<CommonResponse>

    @GET("/api/badges")
    fun getBadgeList(@Header("Authorization") token : String) : Call<CommonResponse>

    @DELETE("/api/users")
    fun deleteUser(@Header("Authorization") token : String) : Call<CommonResponse>

}