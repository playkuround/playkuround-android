package com.umc.playkuround.service

import android.util.Log
import com.umc.playkuround.PlayKuApplication.Companion.user
import com.umc.playkuround.data.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserService {

    open class OnResponseListener {
        open fun <T> getResponseBody(body : T, isSuccess : Boolean, err : String) {
            return
        }
    }

    private var onResponseListener = OnResponseListener()

    fun setOnResponseListener(listener : OnResponseListener) : UserService {
        this.onResponseListener = listener
        return this
    }

    fun register(user : User) {
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)

        userService.register(user).enqueue(object : Callback<UserTokenResponse> {
            override fun onResponse(
                call: Call<UserTokenResponse>,
                response: Response<UserTokenResponse>
            ) {
                Log.d("retrofit", "onResponse: ${response.code()} is received ")
                when(response.code()) {
                    200 -> { // success
                        val resp : UserTokenResponse = response.body()!!
                        onResponseListener.getResponseBody(resp, true, "")
                    }
                    400 -> { // failed
                        val err = JSONObject(response.errorBody()?.string()).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                }
            }

            override fun onFailure(call: Call<UserTokenResponse>, t: Throwable) {
                Log.e("retrofit", "onResponse: fail register $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun isDuplicate(nickname : String) {
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)

        userService.isDuplicate(nickname).enqueue(object : Callback<DuplicateResponse> {
            override fun onResponse(
                call: Call<DuplicateResponse>,
                response: Response<DuplicateResponse>
            ) {
                val resp : DuplicateResponse = response.body()!!
                onResponseListener.getResponseBody(resp, true, "")
            }

            override fun onFailure(call: Call<DuplicateResponse>, t: Throwable) {
                Log.e("retrofit", "onResponse: fail register $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun login(token : String) {
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)

        userService.login(token).enqueue(object : Callback<UserTokenResponse> {
            override fun onResponse(
                call: Call<UserTokenResponse>,
                response: Response<UserTokenResponse>
            ) {
                Log.d("retrofit", "onResponse: ${response.code()} is received ")
                when(response.code()) {
                    200 -> { // success
                        val resp : UserTokenResponse = response.body()!!
                        onResponseListener.getResponseBody(resp, true, "")
                    }
                    401 -> { // failed
                        val err = JSONObject(response.errorBody()?.string()).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                }
            }

            override fun onFailure(call: Call<UserTokenResponse>, t: Throwable) {
                Log.e("retrofit", "onResponse: fail register $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }

        })
    }

    fun logout(token : String) {
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)

        userService.logout(token).enqueue(object : Callback<LogoutResponse> {
            override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>) {
                when(response.code()) {
                    200 -> { // success
                        onResponseListener.getResponseBody(response.body()!!, true, "")
                    }
                    401 -> { // failed
                        val err = JSONObject(response.errorBody()?.string()).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                Log.e("retrofit", "onResponse: fail register $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun reissuanceToken(token : String) {
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)

        userService.reissuanceToken(token).enqueue(object : Callback<RefreshTokenResponse> {
            override fun onResponse(
                call: Call<RefreshTokenResponse>,
                response: Response<RefreshTokenResponse>
            ) {
                Log.d("retrofit", "onResponse: ${response.code()} is received ")
                when(response.code()) {
                    200 -> { // success
                        val resp : RefreshTokenResponse = response.body()!!
                        onResponseListener.getResponseBody(resp, true, "")
                    }
                    401 -> { // failed
                        val err = JSONObject(response.errorBody()?.string()).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                }
            }

            override fun onFailure(call: Call<RefreshTokenResponse>, t: Throwable) {
                Log.e("retrofit", "onResponse: fail register $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun sendEmail(email : String) {
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)

        userService.sendEmail(email).enqueue(object : Callback<EmailResponse> {
            override fun onResponse(call: Call<EmailResponse>, response: Response<EmailResponse>) {
                when(response.code()) {
                    200 -> { // success
                        val resp : EmailResponse = response.body()!!
                        onResponseListener.getResponseBody(resp, true, "")
                    }
                    400, 429 -> { // bad request, too many request
                        val err = JSONObject(response.errorBody()?.string()).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                }
            }

            override fun onFailure(call: Call<EmailResponse>, t: Throwable) {
                Log.e("retrofit", "onResponse: fail register $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun certifyCode(email : String, code : String) {
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)

        userService.certifyCode(email, code).enqueue(object : Callback<EmailCertifyResponse> {
            override fun onResponse(
                call: Call<EmailCertifyResponse>,
                response: Response<EmailCertifyResponse>
            ) {
                if(response.body() == null) {
                    onResponseListener.getResponseBody(null, false, "서버의 응답이 올바르지 않습니다.")
                } else {
                    val resp: EmailCertifyResponse = response.body()!!
                    onResponseListener.getResponseBody(resp, true, "")
                }
            }

            override fun onFailure(call: Call<EmailCertifyResponse>, t: Throwable) {
                Log.e("retrofit", "onResponse: fail register $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

}