package com.umc.playkuround.network

import android.util.Log
import com.google.gson.internal.LinkedTreeMap
import com.umc.playkuround.util.PlayKuApplication.Companion.user
import com.umc.playkuround.data.*
import com.umc.playkuround.util.PlayKuApplication.Companion.pref
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserAPI {

    open class OnResponseListener {
        open fun <T> getResponseBody(body : T, isSuccess : Boolean, err : String) {
            return
        }
    }

    private var onResponseListener = OnResponseListener()

    fun setOnResponseListener(listener : OnResponseListener) : UserAPI {
        this.onResponseListener = listener
        return this
    }

    fun register(user : User) {
        val userAPI = getRetrofit().create(UserRetrofitInterface::class.java)

        userAPI.register(user).enqueue(object : Callback<UserTokenResponse> {
            override fun onResponse(
                call: Call<UserTokenResponse>,
                response: Response<UserTokenResponse>
            ) {
                when(response.code()) {
                    201 -> { // success
                        val resp : UserTokenResponse = response.body()!!
                        onResponseListener.getResponseBody(resp, true, "")
                    }
                    else -> { // failed
                        val err = JSONObject(response.errorBody()?.string()!!).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                }
            }

            override fun onFailure(call: Call<UserTokenResponse>, t: Throwable) {
                Log.e("api err", "onResponse: fail register $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun isAvailable(nickname : String) {
        val userAPI = getRetrofit().create(UserRetrofitInterface::class.java)

        userAPI.isAvailable(nickname).enqueue(object : Callback<DuplicateResponse> {
            override fun onResponse(
                call: Call<DuplicateResponse>,
                response: Response<DuplicateResponse>
            ) {
                val resp : DuplicateResponse = response.body()!!
                onResponseListener.getResponseBody(resp, true, "")
            }

            override fun onFailure(call: Call<DuplicateResponse>, t: Throwable) {
                Log.e("api err", "onResponse: fail isDuplicate $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun logout(token : String) {
        val userAPI = getRetrofit().create(UserRetrofitInterface::class.java)

        userAPI.logout(token).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                when(response.code()) {
                    200 -> { // success
                        user = User.getDefaultUser()
                        pref.clearData()
                        onResponseListener.getResponseBody(null, true, "")
                    }
                    else -> { // failed
                        val err = JSONObject(response.errorBody()?.string()!!).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("api err", "onResponse: fail logout $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun getUserInfo(token : String) {
        val userAPI = getRetrofit().create(UserRetrofitInterface::class.java)

        userAPI.getUserInfo(token).enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(
                call: Call<UserProfileResponse>,
                response: Response<UserProfileResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        val resp : UserProfileResponse = response.body()!!
                        user.email = resp.response.email
                        user.nickname = resp.response.nickname
                        user.major = resp.response.major
                        user.highestScore = resp.response.highestScore
                        onResponseListener.getResponseBody(resp, true, "")
                    }
                    else -> {
                        val err = JSONObject(response.errorBody()?.string()!!).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, "서버 오류로 유저 정보를 불러오지 못했습니다.")
                    }
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                Log.e("api err", "onResponse: fail deleteUser $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun getNotification(token : String, version : String) {
        val userAPI = getRetrofit().create(UserRetrofitInterface::class.java)

        userAPI.getNotification(token, version).enqueue(object : Callback<NotificationResponse> {
            override fun onResponse(
                call: Call<NotificationResponse>,
                response: Response<NotificationResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        val resp : NotificationResponse = response.body()!!
                        onResponseListener.getResponseBody(resp, true, "")
                    }
                    else -> {
                        val err = JSONObject(response.errorBody()?.string()!!).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                }
            }

            override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                Log.e("api err", "onResponse: fail deleteUser $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun getGameScores(token : String) {
        val userAPI = getRetrofit().create(UserRetrofitInterface::class.java)

        userAPI.getGameScores(token).enqueue(object : Callback<HighestScoresResponse> {
            override fun onResponse(
                call: Call<HighestScoresResponse>,
                response: Response<HighestScoresResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        val resp : HighestScoresResponse = response.body()!!
                        onResponseListener.getResponseBody(resp, true, "")
                    }
                    else -> {
                        val err = JSONObject(response.errorBody()?.string()!!).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                }
            }

            override fun onFailure(call: Call<HighestScoresResponse>, t: Throwable) {
                Log.e("api err", "onResponse: fail deleteUser $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun fakeDoor(token : String) {
        val userAPI = getRetrofit().create(UserRetrofitInterface::class.java)

        userAPI.fakeDoor(token).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                when (response.code()) {
                    201 -> {
                        val resp : CommonResponse = response.body()!!
                        onResponseListener.getResponseBody(resp, true, "")
                    }
                    else -> {
                        val err = JSONObject(response.errorBody()?.string()!!).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("api err", "onResponse: fail deleteUser $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

}