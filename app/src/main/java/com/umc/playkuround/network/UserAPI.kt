package com.umc.playkuround.network

import android.util.Log
import com.google.gson.internal.LinkedTreeMap
import com.umc.playkuround.util.PlayKuApplication.Companion.user
import com.umc.playkuround.data.*
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
                        onResponseListener.getResponseBody(response.body()!!, true, "")
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
                        onResponseListener.getResponseBody(null, true, "")
                    }
                    else -> {
                        val err = JSONObject(response.errorBody()?.string()!!).getJSONObject("errorResponse").get("message").toString()
                        Log.d("isoo", "onResponse: $err")
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


    // ---------------------------------------------------------------------------------------------------------------------------

    fun updateUserScore(token : String, scoreType : String) {
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)

        userService.updateUserScore(token, scoreType).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                when(response.code()) {
                    201 -> { // success
                        Log.d("attendanceToday", "onResponse: " + response.body().toString())
                        onResponseListener.getResponseBody(null, true, "")
                    }
                    401 -> { // failed
                        val err = JSONObject(response.errorBody()!!.string()).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                    else -> {
                        Log.d("updateUserScore", "onResponse: ${response.code()}")
                    }
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("retrofit", "onResponse: fail getAttendanceDates $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun saveAdventureLog(token : String, landmark : LandMark) {
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)

        userService.saveAdventureLog(token, landmark).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                try {
                    when (response.code()) {
                        201 -> { // success
                            Log.d("saveAdventureLog", "onResponse: " + response.body().toString())

                            /*val res = response.body()!!.response as LinkedTreeMap<String, String>
                            val badgeArr =
                                res["newBadges"] as ArrayList<LinkedTreeMap<String, String>>
                            val userService2 = UserService()
                            for (i in 0 until badgeArr.size) {
                                userService2.setOnResponseListener(object :
                                    UserService.OnResponseListener() {
                                    override fun <T> getResponseBody(
                                        body: T,
                                        isSuccess: Boolean,
                                        err: String
                                    ) {
                                        if (isSuccess) {
                                            Log.d("saveBadge", "getResponseBody: success $body")
                                        } else {
                                            Log.d("saveBadge", "getResponseBody: fail $body")
                                        }
                                    }
                                }).saveBadge(badgeArr[i]["name"].toString())
                            }*/

                            onResponseListener.getResponseBody(null, true, "")
                        }
                        401 -> { // failed
                            //Log.d("ydyd", "onResponse: ${response.errorBody().toString()}")
                            val err = JSONObject(
                                response.errorBody()!!.string()
                            ).getJSONObject("errorResponse").get("message").toString()
                            //Log.d("ydyd", "onResponse: ${JSONObject(response.errorBody()!!.string())}")
                            onResponseListener.getResponseBody(null, false, err)
                        }
                        else -> {
                            Log.d("saveAdventureLog", "onResponse: ${response.code()}")
                            val err = JSONObject(
                                response.errorBody()!!.string()
                            ).getJSONObject("errorResponse").get("message").toString()
                            onResponseListener.getResponseBody(null, false, err)
                        }
                    }
                } catch (e : Exception) {
                    e.printStackTrace()
                    onResponseListener.getResponseBody(null, false, "서버 연결중 오류가 발생했습니다.")
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("retrofit", "onResponse: fail getAttendanceDates $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun getPlaceRank(token : String, landmarkId : Int) {
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)

        userService.getPlaceRank(token, landmarkId).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                try {
                    when (response.code()) {
                        200 -> { // success
                            Log.d("getPlaceRank", "onResponse: " + response.body().toString())
                            val arr =
                                JSONObject(response.body()!!.response.toString()).getJSONArray("top5Users")
                            val list = ArrayList<HashMap<String, String>>()

                            val myMap = HashMap<String, String>()
                            myMap["count"] =
                                JSONObject(response.body()!!.response.toString()).getJSONObject("me")
                                    .get("count").toString().toDouble().toInt().toString()
                            myMap["ranking"] =
                                JSONObject(response.body()!!.response.toString()).getJSONObject("me")
                                    .get("ranking").toString().toDouble().toInt().toString()
                            list.add(myMap)

                            for (i in 0 until arr.length()) {
                                val map = HashMap<String, String>()
                                val obj = JSONObject(arr[i].toString())
                                val nickname = obj.get("nickname").toString()
                                val count = obj.get("count").toString()
                                val userId = obj.get("userId").toString()
                                map["nickname"] = nickname
                                map["count"] = count
                                map["userId"] = userId
                                list.add(map)
                            }

                            onResponseListener.getResponseBody(list, true, "")
                        }
                        500 -> { // failed
                            val err = JSONObject(
                                response.errorBody()!!.string()
                            ).getJSONObject("errorResponse").get("message").toString()
                            onResponseListener.getResponseBody(null, false, err)
                        }
                        else -> {
                            Log.d("getPlaceRank", "onResponse: ${response.code()}")
                        }
                    }
                } catch(e : Exception) {
                    e.printStackTrace()
                    onResponseListener.getResponseBody(null, false, "서버 연결중 오류가 발생했습니다.")
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("retrofit", "onResponse: fail getAttendanceDates $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun saveBadge(badgeType : String) {
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)

        userService.saveBadge(user.getAccessToken(), badgeType).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                when(response.code()) {
                    201 -> { // success
                        Log.d("saveBadge", "onResponse: " + response.body().toString())

                        onResponseListener.getResponseBody(badgeType, true, "")
                    }
                    500 -> { // failed
                        val err = JSONObject(response.errorBody()!!.string()).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                    else -> {
                        Log.d("getPlaceRank", "onResponse: ${response.code()}")
                    }
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("retrofit", "onResponse: fail getAttendanceDates $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun getBadgeList(token : String) {
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)

        userService.getBadgeList(token).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                try {
                    when (response.code()) {
                        200 -> { // success
                            Log.d("getBadgeList", "onResponse: " + response.body().toString())
                            if (response.body() != null && response.body()!!.response != null) {
                                val arr =
                                    response.body()!!.response as ArrayList<LinkedTreeMap<String, String>>
                                Log.d("xdxd", "onResponse: $arr")
                                val list = ArrayList<String>()
                                for (i in 0 until arr.size) {
                                    Log.d("xdxd", "onResponse: " + arr[i]["name"].toString())
                                    list.add(arr[i]["name"].toString())
                                }
                                Log.d("xdxd", "onResponse: $list")
                                onResponseListener.getResponseBody(list, true, "")
                            } else {
                                onResponseListener.getResponseBody(ArrayList<String>(), true, "")
                            }
                        }
                        500 -> { // failed
                            val err = JSONObject(
                                response.errorBody()!!.string()
                            ).getJSONObject("errorResponse").get("message").toString()
                            onResponseListener.getResponseBody(null, false, err)
                        }
                        else -> {
                            Log.d("getBadgeList", "onResponse: ${response.code()}")
                        }
                    }
                } catch (e : Exception) {
                    e.printStackTrace()
                    onResponseListener.getResponseBody(null, false, "서버 연결중 오류가 발생했습니다.")
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("retrofit", "onResponse: fail getAttendanceDates $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun deleteUser(token : String) {
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)

        userService.deleteUser(token).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                when(response.code()) {
                    200-> {
                        onResponseListener.getResponseBody(null, true, "")
                    }
                    else -> {
                        Log.d("deleteUser", "onResponse: ${response.code()}")
                    }
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("retrofit", "onResponse: fail deleteUser $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

}