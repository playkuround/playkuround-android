package com.umc.playkuround.network

import android.util.Log
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScoreAPI {

    open class OnResponseListener {
        open fun <T> getResponseBody(body : T, isSuccess : Boolean, errorLog : String) {
            return
        }
    }

    private var onResponseListener = OnResponseListener()

    fun setOnResponseListener(listener : OnResponseListener) : ScoreAPI {
        this.onResponseListener = listener
        return this
    }

    fun getTop100(token : String) {
        val scoreRetrofit = getRetrofit().create(ScoreRetrofitInterface::class.java)

        scoreRetrofit.getTop100(token).enqueue(object : Callback<Top100Response> {
            override fun onResponse(call: Call<Top100Response>, response: Response<Top100Response>) {
                when(response.code()) {
                    200 -> { // success
                        val resp : Top100Response = response.body()!!
                        onResponseListener.getResponseBody(resp, true, "")
                    }
                    else -> {
                        val err = JSONObject(response.errorBody()?.string()!!).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                }
            }

            override fun onFailure(call: Call<Top100Response>, t: Throwable) {
                Log.e("api err", "onResponse: fail sendEmail $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun getLandmarkTop100(token : String, landmarkId : Int) {
        val scoreRetrofit = getRetrofit().create(ScoreRetrofitInterface::class.java)

        scoreRetrofit.getLandmarkTop100(token, landmarkId).enqueue(object : Callback<Top100Response> {
            override fun onResponse(call: Call<Top100Response>, response: Response<Top100Response>) {
                when(response.code()) {
                    200 -> { // success
                        val resp : Top100Response = response.body()!!
                        onResponseListener.getResponseBody(resp, true, "")
                    }
                    else -> {
                        val err = JSONObject(response.errorBody()?.string()!!).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                }
            }

            override fun onFailure(call: Call<Top100Response>, t: Throwable) {
                Log.e("api err", "onResponse: fail sendEmail $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

}