package com.umc.playkuround.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.umc.playkuround.util.PlayKuApplication.Companion.pref
import com.umc.playkuround.util.PlayKuApplication.Companion.user
import com.umc.playkuround.R
import com.umc.playkuround.data.User
import com.umc.playkuround.databinding.ActivityLoginBinding
import com.umc.playkuround.network.AuthAPI
import com.umc.playkuround.network.NotificationResponse
import com.umc.playkuround.network.ReissueTokens
import com.umc.playkuround.network.UserAPI
import com.umc.playkuround.util.SoundPlayer
import java.util.Timer
import kotlin.concurrent.timer

const val LOCATION_PERMISSION_REQUEST_CODE = 1001

class LoginActivity : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding
    private var bgGif : Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginLoginBtn.setOnClickListener {
            SoundPlayer(this, R.raw.button_click_sound).play()
            if(!isLocationEnabled()) {
                Toast.makeText(applicationContext, "위치 정보를 활성화해주세요!", Toast.LENGTH_SHORT).show()
                openLocationSettings()
                return@setOnClickListener
            }
            if(!checkLocationPermission()) {
                requestLocationPermission()
            }

            bgGif?.cancel()
            user.load(pref)
            Log.d("isoo", "checkLoginInfo: email : ${user.email}, name : ${user.nickname}, major : ${user.major}")
            if(user.major == "null") {
                Log.d("isoo", "checkLoginInfo: ${user.major}")
                if(!checkLocationPermission()) {
                    Toast.makeText(applicationContext, "위치 권한을 허용해주세요!", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this, EmailCertifyActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                val authAPI = AuthAPI()
                val reissueTokens = ReissueTokens(user.userTokenResponse?.tokenData!!.accessToken, user.userTokenResponse?.tokenData!!.refreshToken)
                authAPI.setOnResponseListener(object : AuthAPI.OnResponseListener() {
                    override fun <T> getResponseBody(
                        body: T,
                        isSuccess: Boolean,
                        errorLog: String
                    ) {
                        if(isSuccess) {
                            if(!checkLocationPermission()) {
                                Toast.makeText(applicationContext, "위치 권한을 허용해주세요!", Toast.LENGTH_SHORT).show()
                            } else {
                                val userAPI = UserAPI()
                                userAPI.setOnResponseListener(object : UserAPI.OnResponseListener() {
                                    override fun <T> getResponseBody(
                                        body: T,
                                        isSuccess: Boolean,
                                        err: String
                                    ) {
                                        if(isSuccess) {
                                            if(body is NotificationResponse) {
                                                val badges = ArrayList<String>()
                                                val alarms = ArrayList<String>()
                                                body.notifications.forEach {
                                                    if(it.name == "new_badge") {
                                                        badges.add(it.description)
                                                    } else if(it.name == "alarm") {
                                                        alarms.add(it.description)
                                                    } else if(it.name == "update") {
                                                        Toast.makeText(applicationContext, "어플리케이션을 업데이트 해주세요!", Toast.LENGTH_SHORT).show()
                                                        return
                                                    } else if(it.name == "system_check") {
                                                        Toast.makeText(applicationContext, "서버 점검 중입니다. 나중에 접속해주세요.", Toast.LENGTH_SHORT).show()
                                                        return
                                                    }
                                                }
                                                val intent = Intent(applicationContext, MapActivity::class.java)
                                                intent.putExtra("badge", badges)
                                                intent.putExtra("alarm", alarms)
                                                startActivity(intent)
                                                finish()
                                            }
                                        } else {
                                            Toast.makeText(applicationContext, "네트워크 오류!", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }).getNotification(user.getAccessToken(), packageManager.getPackageInfo(packageName, 0).versionName)
                            }
                        } else {
                            if(errorLog == "A003" || errorLog == "A004") {
                                user = User.getDefaultUser()
                                user.save(pref)
                                Toast.makeText(applicationContext, "다시 로그인 해주세요!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(applicationContext, "네트워크가 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }).reissue(reissueTokens)
            }
        }

        var num = 0
        bgGif = timer(period = 300) {
            runOnUiThread {
                if(num%4 == 0) binding.loginBackgroundIv.setImageResource(R.drawable.login_bg01)
                else if(num%4 == 1) binding.loginBackgroundIv.setImageResource(R.drawable.login_bg02)
                else if(num%4 == 2) binding.loginBackgroundIv.setImageResource(R.drawable.login_bg03)
                else if(num%4 == 3) binding.loginBackgroundIv.setImageResource(R.drawable.login_bg04)
                num++
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun openLocationSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        this.startActivity(intent)
    }

    private fun checkLocationPermission() : Boolean {
        val fineLocationPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return !(!fineLocationPermissionGranted || !coarseLocationPermissionGranted)
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

}