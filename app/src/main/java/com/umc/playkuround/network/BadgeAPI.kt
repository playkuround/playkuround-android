package com.umc.playkuround.network

import android.util.Log
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BadgeAPI {

    open class OnResponseListener {
        open fun <T> getResponseBody(body : T, isSuccess : Boolean, errorLog : String) {
            return
        }
    }

    private var onResponseListener = OnResponseListener()

    fun setOnResponseListener(listener : OnResponseListener) : BadgeAPI {
        this.onResponseListener = listener
        return this
    }

    fun getUserBadges(token : String) {
        val badgeRetrofit = getRetrofit().create(BadgeRetrofitInterface::class.java)

        badgeRetrofit.getUserBadges(token).enqueue(object : Callback<UserBadgeResponse> {
            override fun onResponse(call: Call<UserBadgeResponse>, response: Response<UserBadgeResponse>) {
                when(response.code()) {
                    200 -> { // success
                        val resp : UserBadgeResponse = response.body()!!
                        onResponseListener.getResponseBody(resp, true, "")
                    }
                    else -> {
                        val err = JSONObject(response.errorBody()?.string()!!).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                }
            }

            override fun onFailure(call: Call<UserBadgeResponse>, t: Throwable) {
                Log.e("api err", "onResponse: fail sendEmail $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun manuallyAddBadge(body : ManualBadgeData) {
        val badgeRetrofit = getRetrofit().create(BadgeRetrofitInterface::class.java)

        badgeRetrofit.manuallyAddBadge(body).enqueue(object : Callback<BadgeResultResponse> {
            override fun onResponse(call: Call<BadgeResultResponse>, response: Response<BadgeResultResponse>) {
                when(response.code()) {
                    201 -> { // success
                        onResponseListener.getResponseBody(null, true, "")
                    }
                    else -> {
                        val err = JSONObject(response.errorBody()?.string()!!).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                }
            }

            override fun onFailure(call: Call<BadgeResultResponse>, t: Throwable) {
                Log.e("api err", "onResponse: fail sendEmail $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun addDreamOfDuckBadge(token : String) {
        val badgeRetrofit = getRetrofit().create(BadgeRetrofitInterface::class.java)

        badgeRetrofit.addDreamOfDuckBadge(token).enqueue(object : Callback<BadgeResultResponse> {
            override fun onResponse(call: Call<BadgeResultResponse>, response: Response<BadgeResultResponse>) {
                when(response.code()) {
                    201 -> { // success
                        onResponseListener.getResponseBody(null, true, "")
                    }
                    else -> {
                        val err = JSONObject(response.errorBody()?.string()!!).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                }
            }

            override fun onFailure(call: Call<BadgeResultResponse>, t: Throwable) {
                Log.e("api err", "onResponse: fail sendEmail $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

}