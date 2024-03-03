package com.umc.playkuround.network

import android.util.Log
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LandmarkAPI {

    open class OnResponseListener {
        open fun <T> getResponseBody(body : T, isSuccess : Boolean, errorLog : String) {
            return
        }
    }

    private var onResponseListener = OnResponseListener()

    fun setOnResponseListener(listener : OnResponseListener) : LandmarkAPI {
        this.onResponseListener = listener
        return this
    }

    fun findLandmark(token : String, latitude : String, longitude : String) {
        val landmarkRetrofit = getRetrofit().create(LandmarkRetrofitInterface::class.java)

        landmarkRetrofit.findLandmark(token, latitude, longitude).enqueue(object : Callback<LandmarkResponse> {
            override fun onResponse(call: Call<LandmarkResponse>, response: Response<LandmarkResponse>) {
                when(response.code()) {
                    200 -> { // success
                        Log.d("isoo", "onResponse: ${response.body()}")
                        val resp : LandmarkResponse = response.body()!!
                        onResponseListener.getResponseBody(resp, true, "")
                    }
                    else -> {
                        val err = JSONObject(response.errorBody()?.string()!!).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                }
            }

            override fun onFailure(call: Call<LandmarkResponse>, t: Throwable) {
                Log.e("api err", "onResponse: fail sendEmail $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun findTopPlayer(token : String, landmarkId : Int) {
        val landmarkRetrofit = getRetrofit().create(LandmarkRetrofitInterface::class.java)

        landmarkRetrofit.findTopPlayer(token, landmarkId).enqueue(object : Callback<LandmarkTopResponse> {
            override fun onResponse(call: Call<LandmarkTopResponse>, response: Response<LandmarkTopResponse>) {
                when(response.code()) {
                    200 -> { // success
                        Log.d("isoo", "onResponse: ${response.body()}")
                        val resp : LandmarkTopResponse = response.body()!!
                        onResponseListener.getResponseBody(resp, true, "")
                    }
                    else -> {
                        val err = JSONObject(response.errorBody()?.string()!!).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                }
            }

            override fun onFailure(call: Call<LandmarkTopResponse>, t: Throwable) {
                Log.e("api err", "onResponse: fail sendEmail $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun sendScore(token : String, adventureData : AdventureData) {
        val landmarkRetrofit = getRetrofit().create(LandmarkRetrofitInterface::class.java)

        landmarkRetrofit.sendScore(token, adventureData).enqueue(object : Callback<GetBadgeResponse> {
            override fun onResponse(call: Call<GetBadgeResponse>, response: Response<GetBadgeResponse>) {
                when(response.code()) {
                    201 -> { // success
                        Log.d("isoo", "onResponse: ${response.body()}")
                        val resp : GetBadgeResponse = response.body()!!
                        onResponseListener.getResponseBody(resp, true, "")
                    }
                    else -> {
                        val err = JSONObject(response.errorBody()?.string()!!).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                }
            }

            override fun onFailure(call: Call<GetBadgeResponse>, t: Throwable) {
                Log.e("api err", "onResponse: fail sendEmail $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

}