package com.umc.playkuround.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.umc.playkuround.util.PlayKuApplication.Companion.user
import com.umc.playkuround.R
import com.umc.playkuround.data.Badge
import com.umc.playkuround.data.LandMark
import com.umc.playkuround.databinding.ActivityMapBinding
import com.umc.playkuround.dialog.BadgeInfoDialog
import com.umc.playkuround.dialog.LoadingDialog
import com.umc.playkuround.dialog.LogoutDialog
import com.umc.playkuround.dialog.MapPlaceDialog
import com.umc.playkuround.dialog.PlaceInfoDialog
import com.umc.playkuround.dialog.RandomGameDialog
import com.umc.playkuround.dialog.StoryDialog
import com.umc.playkuround.network.AuthAPI
import com.umc.playkuround.network.BadgeAPI
import com.umc.playkuround.network.LandmarkAPI
import com.umc.playkuround.network.LandmarkResponse
import com.umc.playkuround.network.LandmarkTopResponse
import com.umc.playkuround.network.ReissueTokens
import com.umc.playkuround.network.ScoreAPI
import com.umc.playkuround.network.Top100Response
import com.umc.playkuround.network.UserAPI
import com.umc.playkuround.network.UserBadgeResponse
import com.umc.playkuround.util.GpsTracker
import com.umc.playkuround.util.PlayKuApplication.Companion.exploredLandmarks
import com.umc.playkuround.util.PlayKuApplication.Companion.pref
import com.umc.playkuround.util.PlayKuApplication.Companion.userTotalScore
import com.umc.playkuround.util.SoundPlayer
import java.text.NumberFormat
import java.util.*
import kotlin.random.Random


class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    lateinit var binding : ActivityMapBinding
    private lateinit var map : GoogleMap
    private lateinit var locationCallback : LocationCallback
    private var visitedList = ArrayList<LandMark>()

    private var myLocation : Marker? = null
    private var nowLocation : LatLng = LatLng(0.0, 0.0)
    private lateinit var gpsTracker : GpsTracker
    private var timer : Timer? = null

    private lateinit var loadingDialog : LoadingDialog

    private val gameLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK) {
            val data : Intent = result.data!!

            val badges = data.getStringArrayListExtra("badge")!!
            badges.forEach {
                val badge = Badge(-1, it, "")
                val badgeInfoDialog = BadgeInfoDialog(this@MapActivity, badge.id)
                badgeInfoDialog.setStatus(false, true)
                badgeInfoDialog.show()
            }

            if(data.getBooleanExtra("isNewLandmark", false)) {
                if(exploredLandmarks.size == 6) {
                    val badgeAPI = BadgeAPI()
                    badgeAPI.addDreamOfDuckBadge(user.getAccessToken())
                    val badgeInfoDialog = BadgeInfoDialog(this@MapActivity, 34)
                    badgeInfoDialog.setStatus(false, true)
                    badgeInfoDialog.show()
                }

                val storyDialog = StoryDialog(this)
                storyDialog.show()
            }
        } else if(result.resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(applicationContext, "게임 점수가 반영되지 않았습니다!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showNotification() {
        val alarms = intent.getStringArrayListExtra("alarm")
        alarms?.forEach {
            val infoDialog = Dialog(this, R.style.TransparentDialogTheme)
            infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            infoDialog.setContentView(R.layout.dialog_ranking_info)
            infoDialog.findViewById<TextView>(R.id.dialog_ranking_info_title).text = "알림"
            infoDialog.findViewById<TextView>(R.id.dialog_ranking_info_context).text = it
            infoDialog.show()
        }

        val badges = intent.getStringArrayListExtra("badge")
        badges?.forEach {
            val badge = Badge(-1, it, "")
            val badgeInfoDialog = BadgeInfoDialog(this@MapActivity, badge.id)
            badgeInfoDialog.setStatus(false, true)
            badgeInfoDialog.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showNotification()

        loadingDialog = LoadingDialog(this)
        loadingDialog.show()

        binding.mapMapFragment.onCreate(savedInstanceState)
        binding.mapMapFragment.getMapAsync(this@MapActivity)

        binding.mapAttendanceBtn.setOnClickListener {
            SoundPlayer(this, R.raw.button_click_sound).play()
            val intent = Intent(applicationContext, AttendanceActivity::class.java)
            intent.putExtra("latitude", nowLocation.latitude)
            intent.putExtra("longitude", nowLocation.longitude)
            startActivity(intent)
        }

        binding.mapRankingBtn.setOnClickListener {
            SoundPlayer(this, R.raw.button_click_sound).play()
            val intent = Intent(applicationContext, RankingActivity::class.java)
            startActivity(intent)
        }

        binding.mapBadgeBtn.setOnClickListener {
            SoundPlayer(this, R.raw.button_click_sound).play()
            val intent = Intent(applicationContext, BadgeActivity::class.java)
            startActivity(intent)
        }

        binding.mapMyBtn.setOnClickListener {
            SoundPlayer(this, R.raw.button_click_sound).play()
            val intent = Intent(applicationContext, MyPageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setUserData() {
        binding.mapNicknameTv.text = user.nickname

        val scoreAPI = ScoreAPI()
        scoreAPI.setOnResponseListener(object : ScoreAPI.OnResponseListener(){
            @SuppressLint("SetTextI18n")
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, errorLog: String) {
                if(isSuccess) {
                    if(body is Top100Response) {
                        val formatter = NumberFormat.getNumberInstance()
                        binding.mapRankingInfoTv.text = formatter.format(body.responseData.myRank.ranking) + "위"

                        if(body.responseData.myRank.ranking == 0)
                            binding.mapRankingInfoTv.text = "- 위"

                        userTotalScore = body.responseData.myRank.score
                        pref.setInt("score", userTotalScore)
                    }
                } else {
                    binding.mapRankingInfoTv.text = "- 위"
                }
            }
        }).getTop100(user.getAccessToken())

        val badgeAPI = BadgeAPI()
        badgeAPI.setOnResponseListener(object : BadgeAPI.OnResponseListener() {
            @SuppressLint("SetTextI18n")
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, errorLog: String) {
                if(isSuccess) {
                    if(body is UserBadgeResponse) {
                        binding.mapBadgeInfoTv.text = "${body.badges.size}개"
                    }
                } else {
                    binding.mapBadgeInfoTv.text = "0개"
                }
            }
        }).getUserBadges(user.getAccessToken())
    }

    override fun onDestroy() {
        timer?.let { timer?.cancel() }
        gpsTracker.stopLocationUpdates()
        if(loadingDialog.isShowing)
            loadingDialog.dismiss()
        super.onDestroy()
    }

    private fun updatingNowLocation(location: Location) {
        val lat = location.latitude
        val lon = location.longitude
        if(loadingDialog.isShowing) {
            Log.d("isoo127", "updatingNowLocation: $lat, $lon")
            loadingDialog.dismiss()
        }

        val bitmapDraw = ResourcesCompat.getDrawable(resources, R.drawable.map_duck, null) as BitmapDrawable
        val b = bitmapDraw.bitmap
        val smallMarker = Bitmap.createScaledBitmap(b, 124, 144, false)

        val markerOptions = MarkerOptions()
        nowLocation = LatLng(lat, lon)

        markerOptions.position(nowLocation)
        markerOptions.snippet("-1")
        markerOptions.zIndex(10f)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))

        runOnUiThread {
            myLocation = if(myLocation != null) {
                myLocation!!.remove()
                map.addMarker(markerOptions)
            } else {
                map.addMarker(markerOptions)
            }
        }
    }

    private fun startRandomGame(landmarkId : Int) {
        fun startGameActivity(intent : Intent) {
            val landmark = LandMark(landmarkId, 0.0,0.0,"",0.0,"")
            intent.putExtra("landmarkId", landmarkId)
            intent.putExtra("latitude", landmark.latitude)
            intent.putExtra("longitude", landmark.longitude)

            if(exploredLandmarks.size < 6) {
                if (exploredLandmarks.contains(landmark.name)) {
                    intent.putExtra("isNewLandmark", false)
                } else {
                    exploredLandmarks.add(landmark.name)
                    pref.setStringSet("exploredLandmarks", exploredLandmarks)
                    intent.putExtra("isNewLandmark", true)
                }
            } else {
                intent.putExtra("isNewLandmark", false)
            }

            gameLauncher.launch(intent)
        }

        val randomGameDialog = RandomGameDialog(this, landmarkId)
        randomGameDialog.setOnStartListener(object : RandomGameDialog.OnStartListener {
            override fun onStart(selected: Int) {
                val intent : Intent = when(selected) {
                    1 -> Intent(applicationContext, MiniGameCardFlippingActivity::class.java)
                    2 -> Intent(applicationContext, MiniGameCatchActivity::class.java)
                    3 -> Intent(applicationContext, MiniGameTypingActivity::class.java)
                    4 -> Intent(applicationContext, MiniGameBridgeActivity::class.java)
                    5 -> Intent(applicationContext, MiniGameMoonActivity::class.java)
                    6 -> Intent(applicationContext, MiniGameAvoidActivity::class.java)
                    7 -> Intent(applicationContext, MiniGameQuizActivity::class.java)
                    8 -> Intent(applicationContext, MiniGameTimerActivity::class.java)
                    else -> Intent(applicationContext, MiniGameTypingActivity::class.java)
                }
                startGameActivity(intent)
            }
        })
        randomGameDialog.show()
    }

    private fun exploreBtnClicked(idx : Int) {
        binding.mapExploreBtn.setOnClickListener {
            SoundPlayer(this, R.raw.button_click_sound).play()
            gpsTracker.requestLastLocation()
            val loading = LoadingDialog(this)
            loading.show()

            val landmarkAPI = LandmarkAPI()
            landmarkAPI.setOnResponseListener(object : LandmarkAPI.OnResponseListener() {
                override fun <T> getResponseBody(body: T, isSuccess: Boolean, errorLog: String) {
                    loading.dismiss()
                    if(isSuccess) {
                        if(body is LandmarkResponse) {
                            if(body.response.landmarkId == 0) {
                                Toast.makeText(applicationContext, "근처에 랜드마크가 없습니다.", Toast.LENGTH_SHORT).show()
                                return
                            }
                            startRandomGame(body.response.landmarkId)
                        }
                    } else {
                        Toast.makeText(applicationContext, errorLog, Toast.LENGTH_SHORT).show()
                    }
                }
            }).findLandmark(user.getAccessToken(), nowLocation.latitude.toString(), nowLocation.longitude.toString())
            Log.d("isoo", "startGameActivity: " + nowLocation.latitude.toString() + "/" + nowLocation.longitude.toString())
        }

        if(idx == -1) {
            binding.mapExploreBtn.setOnClickListener {
                val intent = Intent(applicationContext, MiniGameMoonActivity::class.java)
                intent.putExtra("landmark", LandMark(1, 123.2131, 321.1234, "", 0.0, ""))
                startActivity(intent)
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
        initMap()
        gpsTracker.startLocationUpdates()
    }

    private fun initMap() {
        map.setMinZoomPreference(15.5f)
        map.setMaxZoomPreference(18.0f)

        map.mapType = GoogleMap.MAP_TYPE_TERRAIN

        val kuBound = LatLngBounds(
            LatLng(37.537420146943006, 127.06821864009382),
            LatLng(37.545348706514964, 127.08474104762556)
        )
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(kuBound.center, 16f))
        map.setLatLngBoundsForCameraTarget(kuBound)

        map.uiSettings.isCompassEnabled = false
        map.uiSettings.isMapToolbarEnabled = false

        setMarker()
        map.setOnMarkerClickListener(this)

        map.setPadding(5000,5000,0,0)
    }

    private fun setMarker() {
        for(i in 1..44) {
            if(i != 36)
                visitedList.add(LandMark(i, 0.0, 0.0, "", 0.0, ""))
        }

        visitedList.forEach { l ->
            val landmark = LatLng(l.latitude, l.longitude)

            val bitmapDraw =
                ResourcesCompat.getDrawable(resources, R.drawable.img_flag, null) as BitmapDrawable
            val b = bitmapDraw.bitmap
            val smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false)

            val markerOptions = MarkerOptions()

            markerOptions.position(landmark)
            markerOptions.title(l.name)
            markerOptions.snippet("${l.id}")
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))

            map.addMarker(markerOptions)
        }
    }

    override fun onBackPressed() {
        val logoutDialog = LogoutDialog(this)
        logoutDialog.setOnSelectListener(object : LogoutDialog.OnSelectListener {
            override fun yes() {
                finishAffinity()
            }
        })
        logoutDialog.show()
        logoutDialog.setContext("정말 종료하시겠습니까?")
    }

    override fun onResume() {
        val reissueTokens = ReissueTokens(user.userTokenResponse?.tokenData!!.accessToken, user.userTokenResponse?.tokenData!!.refreshToken)
        AuthAPI().reissue(reissueTokens)

        setUserData()

        Log.d("isoo", "onResume: trigger!")
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                //val nowLocation = LatLng(p0.lastLocation!!.latitude, p0.lastLocation!!.longitude)
                //map.moveCamera(CameraUpdateFactory.newLatLngZoom(nowLocation, 18f))
            }
        }

        gpsTracker = GpsTracker(applicationContext, object : GpsTracker.OnLocationUpdateListener {
            override fun onLocationUpdated(location: Location) {
                updatingNowLocation(location)
//                val location2 = Location("isoo")
//                val landmark = LandMark(Random.nextInt(45), 0.0,0.0,"",0.0,"")
//                location2.latitude = landmark.latitude
//                location2.longitude = landmark.longitude
//                updatingNowLocation(location2)
            }
        })

        exploreBtnClicked(-10)

        val client = LocationServices.getFusedLocationProviderClient(this)// FusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            client.requestLocationUpdates(createLocationRequest(), locationCallback, Looper.getMainLooper())
        }
        super.onResume()
    }

    private fun createLocationRequest(): LocationRequest {
        return LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 0).build()
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        placeInfoDialog(p0.snippet!!.toInt())
        return true
    }

    private fun placeInfoDialog(id : Int) {
        if(id == -1) return
        val landmark = LandMark(id, 0.0, 0.0, "", 0.0, "")

        val loadingDialog = LoadingDialog(this)
        loadingDialog.show()
        val landmarkAPI = LandmarkAPI()
        landmarkAPI.setOnResponseListener(object : LandmarkAPI.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, errorLog: String) {
                loadingDialog.dismiss()
                if(isSuccess) {
                    if(body is LandmarkTopResponse) {
                        val mapPlaceDialog = MapPlaceDialog(this@MapActivity)
                        mapPlaceDialog.show()
                        mapPlaceDialog.setView(landmark.name, landmark.getImageDrawable(), body.response.nickname, body.response.score)

                        mapPlaceDialog.setOnSelectListener(object : MapPlaceDialog.OnSelectListener {
                            override fun ranking() {
                                mapPlaceDialog.dismiss()
                                val intent = Intent(this@MapActivity, LandmarkRankingActivity::class.java)
                                intent.putExtra("landmarkId", landmark.id)
                                startActivity(intent)
                            }

                            override fun info() {
                                mapPlaceDialog.dismiss()
                                val placeInfoDialog = PlaceInfoDialog(this@MapActivity, landmark.id)
                                placeInfoDialog.show()
                            }
                        })
                    }
                } else {
                    Toast.makeText(applicationContext, errorLog, Toast.LENGTH_SHORT).show()
                }
            }
        }).findTopPlayer(user.getAccessToken(), landmark.id)
    }

}