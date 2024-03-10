package com.umc.playkuround.network

import android.util.Log
import com.umc.playkuround.util.PlayKuApplication.Companion.user
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthAPI {

    open class OnResponseListener {
        open fun <T> getResponseBody(body : T, isSuccess : Boolean, errorLog : String) {
            return
        }
    }

    private var onResponseListener = OnResponseListener()

    fun setOnResponseListener(listener : OnResponseListener) : AuthAPI {
        this.onResponseListener = listener
        return this
    }

    fun sendCode(email : String) {
        val authRetrofit = getRetrofit().create(AuthRetrofitInterface::class.java)

        authRetrofit.sendEmail(email).enqueue(object : Callback<EmailResponse> {
            override fun onResponse(call: Call<EmailResponse>, response: Response<EmailResponse>) {
                when(response.code()) {
                    200 -> { // success
                        val resp : EmailResponse = response.body()!!
                        onResponseListener.getResponseBody(resp, true, "")
                    }
                    else -> {
                        val err = JSONObject(response.errorBody()?.string()!!).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                }
            }

            override fun onFailure(call: Call<EmailResponse>, t: Throwable) {
                Log.e("api err", "onResponse: fail sendEmail $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun certifyCode(email : String, code : String) {
        val authRetrofit = getRetrofit().create(AuthRetrofitInterface::class.java)

        authRetrofit.certifyCode(email, code).enqueue(object : Callback<CertifyCodeResponse> {
            override fun onResponse(
                call: Call<CertifyCodeResponse>,
                response: Response<CertifyCodeResponse>
            ) {
                when(response.code()) {
                    200 -> { // success
                        val resp : CertifyCodeResponse = response.body()!!
                        Log.d("isoo", "onResponse: ${resp}")
                        onResponseListener.getResponseBody(resp, true, "")
                    }
                    else -> {
                        val err = JSONObject(response.errorBody()?.string()!!).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                }
            }

            override fun onFailure(call: Call<CertifyCodeResponse>, t: Throwable) {
                Log.e("api err", "onResponse: fail certifyCode $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun reissue(token : ReissueTokens) {
        val authRetrofit = getRetrofit().create(AuthRetrofitInterface::class.java)
        Log.d("isoo", "reissue: $token")

        authRetrofit.reissue(token).enqueue(object : Callback<UserTokenResponse> {
            override fun onResponse(
                call: Call<UserTokenResponse>,
                response: Response<UserTokenResponse>
            ) {
                when(response.code()) {
                    200 -> { // success
                        Log.d("isoo", "onResponse1: ${response.body()}")
                        val resp : TokenData = response.body()!!.tokenData!!
                        Log.d("isoo", "onResponse2: $resp")
                        if(user.userTokenResponse == null)
                            user.userTokenResponse = UserTokenResponse(true, resp.copy())
                        else
                            user.userTokenResponse!!.tokenData = resp.copy()
                        onResponseListener.getResponseBody(null, true, "")
                    }
                    else -> {
                        val err = JSONObject(response.errorBody()?.string()!!).getJSONObject("errorResponse").get("code").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                }
            }

            override fun onFailure(call: Call<UserTokenResponse>, t: Throwable) {
                Log.e("api err", "onResponse: fail certifyCode $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

}