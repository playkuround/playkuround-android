package com.umc.playkuround.service

import android.util.Log
import com.google.gson.internal.LinkedTreeMap
import com.umc.playkuround.PlayKuApplication.Companion.user
import com.umc.playkuround.data.*
import org.json.JSONArray
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
                        val err = JSONObject(response.errorBody()?.string()!!).getJSONObject("errorResponse").get("message").toString()
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
                Log.e("retrofit", "onResponse: fail isDuplicate $call")
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
                        val err = JSONObject(response.errorBody()?.string()!!).getJSONObject("errorResponse").get("code").toString()
                        //Log.d("login_test", "onResponse: " + response.errorBody()!!.string())
                        onResponseListener.getResponseBody(null, false, err)
                    }
                }
            }

            override fun onFailure(call: Call<UserTokenResponse>, t: Throwable) {
                Log.e("retrofit", "onResponse: fail login $call")
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
                        val err = JSONObject(response.errorBody()?.string()!!).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                Log.e("retrofit", "onResponse: fail logout $call")
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
                        val err = JSONObject(response.errorBody()?.string()!!).getJSONObject("errorResponse").get("code").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                }
            }

            override fun onFailure(call: Call<RefreshTokenResponse>, t: Throwable) {
                Log.e("retrofit", "onResponse: fail reissuanceToken $call")
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
                        val err = JSONObject(response.errorBody()?.string()!!).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                }
            }

            override fun onFailure(call: Call<EmailResponse>, t: Throwable) {
                Log.e("retrofit", "onResponse: fail sendEmail $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun certifyCode(email : String, code : String) {
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)

        userService.certifyCode(email, code).enqueue(object : Callback<UserTokenResponse> {
            override fun onResponse(
                call: Call<UserTokenResponse>,
                response: Response<UserTokenResponse>
            ) {
                Log.d("certifyCode", "onResponse: ${response.body()}")
                if(response.body() == null) {
                    onResponseListener.getResponseBody(null, false, "서버의 응답이 올바르지 않습니다.")
                } else {
                    val resp: UserTokenResponse = response.body()!!
                    onResponseListener.getResponseBody(resp, true, "")
                }
            }

            override fun onFailure(call: Call<UserTokenResponse>, t: Throwable) {
                Log.e("retrofit", "onResponse: fail certifyCode $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun attendanceToday(token : String , location : Location) {
        Log.d("attendanceToday", "attendanceToday: start")
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)

        userService.attendanceToday(token, location).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                when(response.code()) {
                    200 -> { // success
                        Log.d("attendanceToday", "onResponse: " + response.body().toString())

                        val res = response.body()!!.response as LinkedTreeMap<String, String>
                        val badgeArr = res["newBadges"] as ArrayList<LinkedTreeMap<String, String>>
                        val userService2 = UserService()
                        for(i in 0 until badgeArr.size) {
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
                        }

                        onResponseListener.getResponseBody(response.body()!!, true, "")
                    }
                    401, 400 -> { // failed
                        //Log.d("attendanceToday", "onResponse: " + response.errorBody()!!.string())
//                        val bst = response.errorBody()!!.byteString().toByteArray()
//                        val str = String(bst, StandardCharsets.UTF_8)
//                        Log.d("test", "onResponse: $str")
//                        val jobj = JSONObject(str)
//                        val errRes = jobj.getJSONObject("errorResponse")
//                        val err = errRes.get("message").toString()
//                        //val err = "response : " + response.errorBody()!!.string()
                        Log.d("attendance", "onResponse: ${response.errorBody()}")
                        onResponseListener.getResponseBody(null, false, "건국대학교 내에 위치하고 있지 않습니다.")
                    }
                    else -> {
                        Log.d("attendance", "onResponse: ${response.code()}")
                    }
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("retrofit", "onResponse: fail attendanceToday $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun getAttendanceDates(token : String) {
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)

        userService.getAttendanceDates(token).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                if(response.body() == null) {
                    onResponseListener.getResponseBody(null, false, "서버의 응답이 올바르지 않습니다.")
                } else {
                    val resp = JSONObject(response.body()!!.response.toString()).getJSONArray("attendances")
                    val dates = ArrayList<String>()
                    for(i in 0 until resp.length()) {
                        dates.add(resp.getString(i))
                    }
                    onResponseListener.getResponseBody(dates, true, "")
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("retrofit", "onResponse: fail getAttendanceDates $call")
                t.printStackTrace()
                onResponseListener.getResponseBody(null, false, "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.")
            }
        })
    }

    fun getNearLandmark(token : String, latitude : String, longitude : String) {
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)
        Log.d("near landmark", "getNearLandmark: $latitude, $longitude")

        userService.getNearLandmark(token, latitude, longitude).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                when(response.code()) {
                    200 -> { // success
                        Log.d("attendanceToday", "onResponse: " + response.body().toString())
                        val resp = response.body()!!.response as LinkedTreeMap<String, String>
                        Log.d("xdxd", "onResponse near: $resp")
                        //val resp = JSONObject(response.body()!!.response.toString())
                        if(resp.isEmpty()) {
                            val err = "올바른 위치가 아닙니다."
                            onResponseListener.getResponseBody(null, false, err)
                            return
                        }
                        val id = resp.get("id").toString().toDouble()
                        val name = resp.get("name").toString()
                        val distance = resp.get("distance").toString().toDouble()
                        val gameType = resp.get("gameType").toString()
                        val landmark = LandMark(id.toInt(), latitude.toDouble(), longitude.toDouble(), name, distance, gameType)
                        onResponseListener.getResponseBody(landmark, true, "")
                    }
                    401 -> { // failed
                        val err = JSONObject(response.errorBody()!!.string()).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
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

    fun getUserRanking(token : String) {
        Log.d("getUserRanking", "start: ")
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)

        userService.getUserRanking(token).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                when(response.code()) {
                    200 -> { // success
                        Log.d("getUserRanking", "onResponse: " + response.body().toString())
                        val resp = JSONObject(response.body()!!.response.toString())
                        val ranking = resp.get("ranking").toString().toDouble().toInt()
                        val nickname = resp.get("nickname").toString()
                        val points = resp.get("points").toString().toDouble().toInt()
                        val myRank = Ranking(ranking, nickname, points)
                        onResponseListener.getResponseBody(myRank, true, "")
                    }
                    400 -> { // failed
                        val err = JSONObject(response.errorBody()!!.string()).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
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

    fun getTop100Ranking(token : String) {
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)

        userService.getTop100Ranking(token).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                when(response.code()) {
                    200 -> { // success
                        Log.d("attendanceToday", "onResponse: " + response.body().toString())
                        if(response.body()!!.response != null) {
                            val resp = JSONArray(response.body()!!.response.toString())
                            val top100Rank = ArrayList<Ranking>()
                            for (i in 0 until resp.length()) {
                                val jobj = JSONObject(resp[i].toString())
                                val ranking = jobj.get("ranking").toString().toDouble().toInt()
                                val nickname = jobj.get("nickname").toString()
                                val points = jobj.get("points").toString().toDouble().toInt()
                                val rank = Ranking(ranking, nickname, points)
                                top100Rank.add(rank)
                            }
                            onResponseListener.getResponseBody(top100Rank, true, "")
                        } else {
                            onResponseListener.getResponseBody(null, false, "")
                        }
                    }
                    401 -> { // failed
                        val err = JSONObject(response.errorBody()!!.string()).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
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
                when(response.code()) {
                    201 -> { // success
                        Log.d("saveAdventureLog", "onResponse: " + response.body().toString())

                        val res = response.body()!!.response as LinkedTreeMap<String, String>
                        val badgeArr = res["newBadges"] as ArrayList<LinkedTreeMap<String, String>>
                        val userService2 = UserService()
                        for(i in 0 until badgeArr.size) {
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
                        }

                        onResponseListener.getResponseBody(null, true, "")
                    }
                    401 -> { // failed
                        val err = JSONObject(response.errorBody()!!.string()).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                    else -> {
                        Log.d("saveAdventureLog", "onResponse: ${response.code()}")
                        val err = JSONObject(response.errorBody()!!.string()).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
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

    fun getUserAdventureLog(token : String) {
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)

        userService.getUserAdventureLog(token).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                when(response.code()) {
                    200 -> { // success
                        Log.d("getUserAdventureLog", "onResponse: " + response.body().toString())
                        if(response.body()!!.response != null) {
                            val obj = JSONObject(response.body()!!.response.toString()).getJSONArray("landmarkIdList")
                            //Log.d("getUserAdventureLog", "onResponse: " +  obj[0])

                            val landmarks = ArrayList<LandMark>()
                            for (i in 0 until obj.length()) {
                                val id = obj[i]!!.toString().toDouble().toInt()
                                val landmark = LandMark(id, 0.0, 0.0, "", 0.0, "")
                                landmarks.add(landmark)
                            }
                            onResponseListener.getResponseBody(landmarks, true, "")
                        } else {
                            onResponseListener.getResponseBody(null, false, "")
                        }
                    }
                    500 -> { // failed
                        val err = JSONObject(response.errorBody()!!.string()).getJSONObject("errorResponse").get("message").toString()
                        //Log.d("adventure", "onResponse: ${JSONObject(response.errorBody()!!.string()).getJSONObject("errorResponse").get("code")}")
                        onResponseListener.getResponseBody(null, false, err)
                    }
                    else -> {
                        Log.d("saveAdventureLog", "onResponse: ${response.code()}")
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

    fun getPlaceRank(token : String, landmarkId : Int) {
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)

        userService.getPlaceRank(token, landmarkId).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                when(response.code()) {
                    200 -> { // success
                        Log.d("getPlaceRank", "onResponse: " + response.body().toString())
                        val arr = JSONObject(response.body()!!.response.toString()).getJSONArray("top5Users")
                        val list = ArrayList<HashMap<String, String>>()

                        val myMap = HashMap<String, String>()
                        myMap["count"] = JSONObject(response.body()!!.response.toString()).getJSONObject("me").get("count").toString().toDouble().toInt().toString()
                        myMap["ranking"] = JSONObject(response.body()!!.response.toString()).getJSONObject("me").get("ranking").toString().toDouble().toInt().toString()
                        list.add(myMap)

                        for(i in 0 until arr.length()) {
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
                when(response.code()) {
                    200 -> { // success
                        Log.d("getBadgeList", "onResponse: " + response.body().toString())
                        if(response.body() != null && response.body()!!.response != null) {
                            val arr = response.body()!!.response as ArrayList<LinkedTreeMap<String, String>>
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
                        val err = JSONObject(response.errorBody()!!.string()).getJSONObject("errorResponse").get("message").toString()
                        onResponseListener.getResponseBody(null, false, err)
                    }
                    else -> {
                        Log.d("getBadgeList", "onResponse: ${response.code()}")
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

    fun getUserInfo(token : String) {
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)

        userService.getUserInfo(token).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                when(response.code()) {
                    200-> {
                        val res = JSONObject(response.body()!!.response.toString())
                        user.email = res.getString("email")
                        user.nickname = res.getString("nickname")
                        user.major = res.getString("major")
                        onResponseListener.getResponseBody(null, true, "")
                    }
                    else -> {
                        Log.d("deleteUser", "onResponse: ${response.code()}")
                        onResponseListener.getResponseBody(null, false, "서버 오류로 유저 정보를 불러오지 못했습니다.")
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