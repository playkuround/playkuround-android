package com.umc.playkuround.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import com.umc.playkuround.PlayKuApplication.Companion.user
import com.umc.playkuround.R
import com.umc.playkuround.data.LandMark
import com.umc.playkuround.data.Ranking
import com.umc.playkuround.databinding.ActivityMapBinding
import com.umc.playkuround.dialog.LoadingDialog
import com.umc.playkuround.dialog.MapPlaceDialog
import com.umc.playkuround.dialog.SmallSlideUpDialog
import com.umc.playkuround.service.AvoidView
import com.umc.playkuround.service.GpsTracker
import com.umc.playkuround.service.UserService
import java.util.*


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialog = LoadingDialog(this)
        loadingDialog.show()

        binding.mapMapFragment.onCreate(savedInstanceState)
        binding.mapMapFragment.getMapAsync(this@MapActivity)

        binding.mapAttendanceBtn.setOnClickListener {
            val intent = Intent(applicationContext, AttendanceActivity::class.java)
            startActivity(intent)
        }

        binding.mapRankingBtn.setOnClickListener {
            val intent = Intent(applicationContext, RankingActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        timer?.let { timer?.cancel() }
        gpsTracker.stopLocationUpdates()
        if(loadingDialog.isShowing)
            loadingDialog.dismiss()
        super.onDestroy()
    }

    private fun updatingNowLocation(location: Location) {
        val kuBound = LatLngBounds(
            LatLng(37.5398, 127.071),
            LatLng(37.54499, 127.08515)
        )
/*        if(!kuBound.contains(LatLng(location.getLatitude(), location.getLongitude()))){
            Log.d("gps tracking", "updatingNowLocation: ${location.getLatitude()}, ${location.getLongitude()}")
            Toast.makeText(applicationContext, "건국대학교에 위치하고 있지 않습니다.", Toast.LENGTH_SHORT).show()
            binding.mapClickBtn.visibility = View.INVISIBLE
            return
        }*/

        //gpsTracker.getLocation(applicationContext)
        val lat = location.latitude
        val lon = location.longitude
        if(loadingDialog.isShowing) {
            Log.d("isoo127", "updatingNowLocation: $lat, $lon")
            loadingDialog.dismiss()
        }
        //Log.d("updating now location", "updatingNowLocation: lat : $lat, lon : $lon")

        val bitmapDraw =
            ResourcesCompat.getDrawable(resources, R.drawable.map_duck, null) as BitmapDrawable
        val b = bitmapDraw.bitmap
        val smallMarker = Bitmap.createScaledBitmap(b, 124, 144, false)


        val markerOptions = MarkerOptions()
        nowLocation = LatLng(lat, lon)

        markerOptions.position(nowLocation)
        markerOptions.snippet("-1")
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))

//        val directionIcon = BitmapDescriptorFactory.fromResource(R.drawable.map_direction)
//
//        val rotation = 0f
//        val directionMarkerOptions = MarkerOptions()
//            .position(nowLocation)
//            .icon(directionIcon)
//            .anchor(0.5f, 0.5f)
//            .flat(true)
//            .rotation(rotation)

        runOnUiThread {
            myLocation = if(myLocation != null) {
                myLocation!!.remove()
                map.addMarker(markerOptions)
                //map.addMarker(directionMarkerOptions)
            } else {
                map.addMarker(markerOptions)
                //map.addMarker(directionMarkerOptions)
            }
        //Toast.makeText(applicationContext, "$lat, $lon", Toast.LENGTH_SHORT).show()
        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(nowLocation!!, 17f))
        }
    }

    private fun startGameActivity(idx : Int) {
        binding.mapExploreBtn.setOnClickListener {
            when((1..6).random()) {
                1->{
                    val intent = Intent(applicationContext, MiniGameTimerActivity::class.java)
                    startActivity(intent)
                }
                2->{
                    val intent = Intent(applicationContext, MiniGameAvoidActivity::class.java)
                    startActivity(intent)
                }
                3->{
                    val intent = Intent(applicationContext, MiniGameBridgeActivity::class.java)
                    startActivity(intent)
                }
                4->{
                    val intent = Intent(applicationContext, MiniGameCardFlippingActivity::class.java)
                    startActivity(intent)
                }
                5->{
                    val intent = Intent(applicationContext, MiniGameCatchActivity::class.java)
                    startActivity(intent)
                }
                6->{
                    val intent = Intent(applicationContext, MiniGameTypingActivity::class.java)
                    startActivity(intent)
                }
            }
        }
//        binding.mapExploreBtn.setOnClickListener {
//            gpsTracker.requestLastLocation()
//            val loading = LoadingDialog(this)
//            loading.show()
//
//            val userService = UserService()
//            userService.setOnResponseListener(object : UserService.OnResponseListener() {
//                override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
//                    if(isSuccess) {
//                        loading.dismiss()
//                        Log.d("near_landmark", "getResponseBody: $body")
//                        val landmark = body as LandMark
//                        Log.d("map activity check", "getResponseBody: $landmark")
//                        Log.d("map activity check", "getResponseBody: $visitedList")
//
//                        if(visitedList.contains(LandMark(landmark.id, 0.0, 0.0, "", 0.0, ""))) {
//                            saveAdventureLog(landmark)
//                        } else {
//                            val intent: Intent = when (landmark.gameType) {
//                                LandMark.QUIZ -> {
//                                    Intent(applicationContext, MiniGameQuizActivity::class.java)
//                                }
//                                LandMark.MOON -> {
//                                    Intent(applicationContext, MiniGameMoonActivity::class.java)
//                                }
//                                LandMark.TIMER -> {
//                                    Intent(applicationContext, MiniGameTimerActivity::class.java)
//                                }
//                                else -> {
//                                    Intent(applicationContext, MiniGameTimerActivity::class.java)
//                                }
//                            }
//                            intent.putExtra("landmark", landmark)
//                            startActivity(intent)
//                        }
//                    } else {
//                        loading.dismiss()
//                        Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }).getNearLandmark(user.getAccessToken(), nowLocation.latitude.toString(), nowLocation.longitude.toString())
//            Log.d("xdxd", "startGameActivity: " + nowLocation.latitude.toString() + "/" + nowLocation.longitude.toString())
//        }
//
//        if(idx == -1) {
//            binding.mapExploreBtn.setOnClickListener {
//                //val intent = Intent(applicationContext, MiniGameQuizActivity::class.java)
//                val intent = Intent(applicationContext, MiniGameMoonActivity::class.java)
//                //val intent = Intent(applicationContext, MiniGameTimerActivity::class.java)
//                intent.putExtra("landmark", LandMark(1, 123.2131, 321.1234, "", 0.0, ""))
//                startActivity(intent)
//            }
//        }
    }

    private fun saveAdventureLog(landmark : LandMark) {
        val loading = LoadingDialog(this)
        loading.show()

        val userService = UserService()
        userService.setOnResponseListener(object : UserService.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                if (isSuccess) {
                    Toast.makeText(applicationContext, landmark.name + "에 출석했습니다!", Toast.LENGTH_SHORT).show()

                    val userService2 = UserService()

                    userService2.setOnResponseListener(object : UserService.OnResponseListener() {
                        override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                            if(isSuccess) {
                                loading.dismiss()
                            } else {
                                Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }).updateUserScore(user.getAccessToken(), Ranking.scoreType.EXTRA_ADVENTURE)
                } else {
                    loading.dismiss()
                    Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
                }
            }
        }).saveAdventureLog(user.getAccessToken(), landmark)
    }

    override fun onMapReady(p0: GoogleMap) {
        Log.d("isoo127map", "onMapReady: $p0")
        map = p0
        initMap()
        gpsTracker.startLocationUpdates()
    }

    private fun initMap() {
        map.setMinZoomPreference(15.0f)
        map.setMaxZoomPreference(19.0f)

        val kuBound = LatLngBounds(
            LatLng(37.5398, 127.071),
            LatLng(37.542, 127.082)
        )
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(kuBound.center, 16f))
        //map.setLatLngBoundsForCameraTarget(kuBound)

        map.uiSettings.isCompassEnabled = false
        map.uiSettings.isMapToolbarEnabled = false

        setMarker()
        map.setOnMarkerClickListener(this)
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

//        val userService = UserService()
//        userService.setOnResponseListener(object : UserService.OnResponseListener() {
//            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
//                if(isSuccess) {
//                    loading.dismiss()
//                    Log.d("getUserAdventureLog", "getResponseBody: $body")
//
//                    visitedList = body as ArrayList<LandMark>
//
//                    visitedList.forEach { l ->
//                        val landmark = LatLng(l.latitude, l.longitude)
//
//                        val bitmapDraw = ResourcesCompat.getDrawable(resources, R.drawable.img_flag, null) as BitmapDrawable
//                        val b = bitmapDraw.bitmap
//                        val smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false)
//
//                        val markerOptions = MarkerOptions()
//
//                        markerOptions.position(landmark)
//                        markerOptions.title(l.name)
//                        markerOptions.snippet("${l.id}")
//                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
//
//                        map.addMarker(markerOptions)
//                    }
//                } else {
//                    loading.dismiss()
//                    Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
//                }
//            }
//        }).getUserAdventureLog(user.getAccessToken())
    }

    private fun screenWidth() : Int {
        return resources.displayMetrics.widthPixels
    }

    private fun screenHeight() : Int {
        return resources.displayMetrics.heightPixels
    }

    private fun toPX(dp : Int) : Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    override fun onResume() {
        Log.d("gps", "onResume: start resume")

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                val nowLocation = LatLng(p0.lastLocation!!.latitude, p0.lastLocation!!.longitude)
                Log.d("gps", "onLocationChanged: ${p0.lastLocation!!.latitude} , ${p0.lastLocation!!.longitude}")
                //map.moveCamera(CameraUpdateFactory.newLatLngZoom(nowLocation, 18f))
            }
        }
        Log.d("isoo127", "onResume: $locationCallback")

        gpsTracker = GpsTracker(applicationContext, object : GpsTracker.OnLocationUpdateListener {
            override fun onLocationUpdated(location: Location) {
                Log.d("isoo127", "onLocationUpdated: success")
                updatingNowLocation(location)
            }
        })
        Log.d("isoo127", "onResume: $gpsTracker")

        startGameActivity(-10)

//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map_map_fragment) as SupportMapFragment?
//        mapFragment!!.getMapAsync(this)
//        MapsInitializer.initialize(this)

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
        //        val locationRequest = LocationRequest.create().apply {
//            interval = 0
//            fastestInterval = 0
//            priority = Priority.PRIORITY_HIGH_ACCURACY
//        }

        return LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 0).build()
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        //p0.showInfoWindow()
        placeInfoDialog(p0.snippet!!.toInt())
        return true
    }

    private fun placeInfoDialog(id : Int) {
        if(id == -1) return
        val landmark = LandMark(id, 0.0, 0.0, "", 0.0, "")

        val mapPlaceDialog = MapPlaceDialog(this)
        mapPlaceDialog.show()
        mapPlaceDialog.setView(landmark.name, landmark.getImageDrawable())
    }

}