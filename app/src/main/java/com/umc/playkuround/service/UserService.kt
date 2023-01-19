package com.umc.playkuround.service

import android.util.Log
import com.umc.playkuround.data.*
import com.umc.playkuround.data.Ranking.Companion.scoreType
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Header
import java.nio.charset.StandardCharsets

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
                        val err = JSONObject(response.errorBody()?.string()).getJSONObject("errorResponse").get("code").toString()
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
                        val err = JSONObject(response.errorBody()?.string()).getJSONObject("errorResponse").get("message").toString()
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
                        val err = JSONObject(response.errorBody()?.string()).getJSONObject("errorResponse").get("code").toString()
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
                        val err = JSONObject(response.errorBody()?.string()).getJSONObject("errorResponse").get("message").toString()
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
                        onResponseListener.getResponseBody(response.body()!!, true, "")
                    }
                    401 -> { // failed
                        //Log.d("attendanceToday", "onResponse: " + response.errorBody()!!.string())
//                        val bst = response.errorBody()!!.byteString().toByteArray()
//                        val str = String(bst, StandardCharsets.UTF_8)
//                        Log.d("test", "onResponse: $str")
//                        val jobj = JSONObject(str)
//                        val errRes = jobj.getJSONObject("errorResponse")
//                        val err = errRes.get("message").toString()
//                        //val err = "response : " + response.errorBody()!!.string()
                        onResponseListener.getResponseBody(null, false, "건국대학교 내에 위치하고 있지 않습니다.")
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

        userService.getNearLandmark(token, latitude, longitude).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                when(response.code()) {
                    200 -> { // success
                        Log.d("attendanceToday", "onResponse: " + response.body().toString())
                        val resp = JSONObject(response.body()!!.response.toString())
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

                        // 추후 배지 부분 완성 시 배지 정보 확인 해야함

                        onResponseListener.getResponseBody(null, true, "")
                    }
                    401 -> { // failed
                        val err = JSONObject(response.errorBody()!!.string()).getJSONObject("errorResponse").get("message").toString()
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
                            val resp = JSONArray(response.body()!!.response.toString())
                            val landmarks = ArrayList<LandMark>()
                            for (i in 0 until resp.length()) {
                                val jobj = JSONObject(resp[i].toString())
                                val id = jobj.get("landmarkId").toString().toDouble().toInt()
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

}