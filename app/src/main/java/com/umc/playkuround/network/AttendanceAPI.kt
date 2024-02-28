package com.umc.playkuround.network

import android.util.Log
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AttendanceAPI {

    open class OnResponseListener {
        open fun <T> getResponseBody(body : T, isSuccess : Boolean, errorLog : String) {
            return
        }
    }

    private var onResponseListener = OnResponseListener()

    fun setOnResponseListener(listener : OnResponseListener) : AttendanceAPI {
        this.onResponseListener = listener
        return this
    }

    fun getAttendanceData(token : String) {
        val attendanceRetrofit = getRetrofit().create(AttendanceRetrofitInterface::class.java)

        attendanceRetrofit.getAttendanceData(token).enqueue(object : Callback<AttendanceResponse> {
            override fun onResponse(call: Call<AttendanceResponse>, response: Response<AttendanceResponse>) {
                when(response.code()) {
                    200 -> { // success
                        val resp : AttendanceResponse = response.body()!!
                        onResponseListener.getResponseBody(resp, true, "")
                    }
                    else -> {
                        val err = JSONObject(response.errorBody()?.string()!!).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                }
            }

            override fun onFailure(call: Call<AttendanceResponse>, t: Throwable) {
                Log.e("api err", "onResponse: fail sendEmail $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun attendanceToday(token : String, userLocation : UserLocation) {
        val attendanceRetrofit = getRetrofit().create(AttendanceRetrofitInterface::class.java)

        attendanceRetrofit.attendanceToday(token, userLocation).enqueue(object : Callback<GetBadgeResponse> {
            override fun onResponse(call: Call<GetBadgeResponse>, response: Response<GetBadgeResponse>) {
                when(response.code()) {
                    200 -> { // success
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