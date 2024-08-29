package com.umc.playkuround.network

import android.util.Log
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeAPI {

    open class OnResponseListener {
        open fun <T> getResponseBody(body : T, isSuccess : Boolean, errorLog : String) {
            return
        }
    }

    private var onResponseListener = OnResponseListener()

    fun setOnResponseListener(listener : OnResponseListener) : NoticeAPI {
        this.onResponseListener = listener
        return this
    }

    fun getNotice(token : String) {
        val noticeRetrofit = getRetrofit().create(NoticeRetrofitInterface::class.java)

        noticeRetrofit.getNotice(token).enqueue(object : Callback<NoticeResponse> {
            override fun onResponse(call: Call<NoticeResponse>, response: Response<NoticeResponse>) {
                when(response.code()) {
                    200 -> { // success
                        val resp : NoticeResponse = response.body()!!
                        onResponseListener.getResponseBody(resp.response, true, "")
                    }
                    else -> {
                        val err = JSONObject(response.errorBody()?.string()!!).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                }
            }

            override fun onFailure(call: Call<NoticeResponse>, t: Throwable) {
                Log.e("api err", "onResponse: fail getNotice $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

}