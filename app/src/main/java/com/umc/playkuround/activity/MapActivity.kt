package com.umc.playkuround.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Bundle
import android.os.Looper
import android.telephony.CarrierConfigManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import com.umc.playkuround.PlayKuApplication
import com.umc.playkuround.PlayKuApplication.Companion.user
import com.umc.playkuround.R
import com.umc.playkuround.data.LandMark
import com.umc.playkuround.databinding.ActivityMapBinding
import com.umc.playkuround.dialog.LoadingDialog
import com.umc.playkuround.service.UserService
import kotlin.random.Random


class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    lateinit var binding : ActivityMapBinding
    private lateinit var map : GoogleMap
    private lateinit var locationCallback : LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mapBackBtn.setOnClickListener{
            finish()
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                val nowLocation = LatLng(p0.lastLocation!!.latitude, p0.lastLocation!!.longitude)
                Log.d("gps", "onLocationChanged: ${p0.lastLocation!!.latitude} , ${p0.lastLocation!!.longitude}")
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(nowLocation, 18f))
            }
        }

        startGameActivity(getGames())

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_map_fragment) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        MapsInitializer.initialize(this)
    }

    private fun startGameActivity(idx : Int) {
        binding.mapClickBtn.setOnClickListener {
            val loading = LoadingDialog(this)
            loading.show()

            val userService = UserService()
            userService.setOnResponseListener(object : UserService.OnResponseListener() {
                override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                    if(isSuccess) {
                        loading.dismiss()
                        Log.d("near_landmark", "getResponseBody: $body")
                        val landmark = body as LandMark

                        val intent : Intent = when (landmark.gameType) {
                            LandMark.QUIZ -> {
                                Intent(applicationContext, MiniGameQuizActivity::class.java)
                            }
                            LandMark.MOON -> {
                                Intent(applicationContext, MiniGameMoonActivity::class.java)
                            }
                            LandMark.TIMER -> {
                                Intent(applicationContext, MiniGameTimerActivity::class.java)
                            }
                            else -> {
                                Intent(applicationContext, MiniGameTimerActivity::class.java)
                            }
                        }
                        intent.putExtra("landmark", landmark)
                        startActivity(intent)
                    } else {
                        loading.dismiss()
                        Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
                    }
                }
            }).getNearLandmark(user.getAccessToken(), "37.5424444", "127.077995")

            //val intent = Intent(this, DialogPlaceRankActivity::class.java)
        }
    }

    private fun getGames() : Int {
        return Random(System.currentTimeMillis()).nextInt(3)
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
        initMap()

        map.setPadding(0, screenHeight() - toPX(142), screenWidth() - toPX(70), 0)
    }

    private fun initMap() {
        map.setMinZoomPreference(16.0f)
        map.setMaxZoomPreference(18.0f)

        val kuBound = LatLngBounds(
            LatLng(37.5398, 127.071),
            LatLng(37.542, 127.082)
        )
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(kuBound.center, 16f))
        map.setLatLngBoundsForCameraTarget(kuBound)

        map.uiSettings.isCompassEnabled = false
        map.uiSettings.isMapToolbarEnabled = false

        setMarker()
    }

    private fun setMarker() {
        val landmark = LatLng(37.5399272, 127.0730058)

        val bitmapDraw = ResourcesCompat.getDrawable(resources, R.drawable.img_flag, null) as BitmapDrawable
        val b = bitmapDraw.bitmap
        val smallMarker = Bitmap.createScaledBitmap(b, 60, 90, false)

        val markerOptions = MarkerOptions()

        markerOptions.position(landmark)
        markerOptions.title("산학협동관")
        markerOptions.snippet("건국대학교 건물 #1")
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))

        map.addMarker(markerOptions)
        map.setOnMarkerClickListener(this)
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
        val client = FusedLocationProviderClient(this)
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

    private fun createLocationRequest() : com.google.android.gms.location.LocationRequest {
        val locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
            interval = 0
            fastestInterval = 0
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }

        return locationRequest
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        p0.showInfoWindow()
        return true
    }

}