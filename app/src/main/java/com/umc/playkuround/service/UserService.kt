package com.umc.playkuround.service

import android.util.Log
import com.umc.playkuround.data.RegisterUserResponse
import com.umc.playkuround.data.User
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserService {

    fun register(user : User) {
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)

        userService.register(user).enqueue(object : Callback<RegisterUserResponse> {
            override fun onResponse(
                call: Call<RegisterUserResponse>,
                response: Response<RegisterUserResponse>
            ) {
                Log.d("retrofit", "onResponse: ${response.code()} is received ")
                when(response.code()) {
                    200 -> { // success
                        val resp: RegisterUserResponse = response.body()!!
                        Log.d("retrofit", "onResponse: " + resp.response.accessTokenExpiredAt)
                    }
                    400 -> { // failed
                        val err = JSONObject(response.errorBody()?.string())
                        err.getJSONObject("errorResponse").get("message")
                        Log.d("retrofit", "onResponse: " + err.getJSONObject("errorResponse").get("message").toString())
                    }
                }
            }

            override fun onFailure(call: Call<RegisterUserResponse>, t: Throwable) {
                Log.e("retrofit", "onResponse: fail register $call")
                t.printStackTrace()
            }
        })
    }

}