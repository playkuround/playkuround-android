package com.umc.playkuround.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
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
import com.umc.playkuround.util.PlayKuApplication.Companion.user
import com.umc.playkuround.R
import com.umc.playkuround.data.LandMark
import com.umc.playkuround.databinding.ActivityMapBinding
import com.umc.playkuround.dialog.LoadingDialog
import com.umc.playkuround.dialog.MapPlaceDialog
import com.umc.playkuround.dialog.PlaceInfoDialog
import com.umc.playkuround.network.LandmarkAPI
import com.umc.playkuround.network.LandmarkResponse
import com.umc.playkuround.network.LandmarkTopResponse
import com.umc.playkuround.util.GpsTracker
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
            intent.putExtra("latitude", nowLocation.latitude)
            intent.putExtra("longitude", nowLocation.longitude)
            startActivity(intent)
        }

        binding.mapRankingBtn.setOnClickListener {
            val intent = Intent(applicationContext, RankingActivity::class.java)
            startActivity(intent)
        }

        binding.mapBadgeBtn.setOnClickListener {
            val intent = Intent(applicationContext, BadgeActivity::class.java)
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
//        val kuBound = LatLngBounds(
//            LatLng(37.5398, 127.071),
//            LatLng(37.54499, 127.08515)
//        )
//        if(!kuBound.contains(LatLng(location.latitude, location.longitude))){
//            Toast.makeText(applicationContext, "건국대학교 내부에 위치하고 있지 않습니다.", Toast.LENGTH_SHORT).show()
//            binding.mapExploreBtn.visibility = View.INVISIBLE
//            return
//        }

        val lat = location.latitude
        val lon = location.longitude
        if(loadingDialog.isShowing) {
            Log.d("isoo127", "updatingNowLocation: $lat, $lon")
            loadingDialog.dismiss()
        }

        val bitmapDraw =
            ResourcesCompat.getDrawable(resources, R.drawable.map_duck, null) as BitmapDrawable
        val b = bitmapDraw.bitmap
        val smallMarker = Bitmap.createScaledBitmap(b, 124, 144, false)


        val markerOptions = MarkerOptions()
        nowLocation = LatLng(lat, lon)

        markerOptions.position(nowLocation)
        markerOptions.snippet("-1")
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

    private fun startGameActivity(idx : Int) {
        binding.mapExploreBtn.setOnClickListener {
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
                            when((1..6).random()) {
                                1 -> {
                                    val intent = Intent(applicationContext, MiniGameTimerActivity::class.java)
                                    intent.putExtra("landmarkId", body.response.landmarkId)
                                    intent.putExtra("latitude", nowLocation.latitude)
                                    intent.putExtra("longitude", nowLocation.longitude)
                                    startActivity(intent)
                                }

                                2 -> {
                                    val intent = Intent(applicationContext, MiniGameAvoidActivity::class.java)
                                    intent.putExtra("landmarkId", body.response.landmarkId)
                                    intent.putExtra("latitude", nowLocation.latitude)
                                    intent.putExtra("longitude", nowLocation.longitude)
                                    startActivity(intent)
                                }

                                3 -> {
                                    val intent = Intent(applicationContext, MiniGameBridgeActivity::class.java)
                                    intent.putExtra("landmarkId", body.response.landmarkId)
                                    intent.putExtra("latitude", nowLocation.latitude)
                                    intent.putExtra("longitude", nowLocation.longitude)
                                    startActivity(intent)
                                }

                                4 -> {
                                    val intent = Intent(applicationContext, MiniGameCardFlippingActivity::class.java)
                                    intent.putExtra("landmarkId", body.response.landmarkId)
                                    intent.putExtra("latitude", nowLocation.latitude)
                                    intent.putExtra("longitude", nowLocation.longitude)
                                    startActivity(intent)
                                }

                                5 -> {
                                    val intent = Intent(applicationContext, MiniGameCatchActivity::class.java)
                                    intent.putExtra("landmarkId", body.response.landmarkId)
                                    intent.putExtra("latitude", nowLocation.latitude)
                                    intent.putExtra("longitude", nowLocation.longitude)
                                    startActivity(intent)
                                }

                                6 -> {
                                    val intent = Intent(applicationContext, MiniGameTypingActivity::class.java)
                                    intent.putExtra("landmarkId", body.response.landmarkId)
                                    intent.putExtra("latitude", nowLocation.latitude)
                                    intent.putExtra("longitude", nowLocation.longitude)
                                    startActivity(intent)
                                }
                            }
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
        map.setMinZoomPreference(15.0f)
        map.setMaxZoomPreference(19.0f)

        val kuBound = LatLngBounds(
            LatLng(37.5398, 127.071),
            LatLng(37.542, 127.082)
        )
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(kuBound.center, 16f))

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
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                val nowLocation = LatLng(p0.lastLocation!!.latitude, p0.lastLocation!!.longitude)
                //map.moveCamera(CameraUpdateFactory.newLatLngZoom(nowLocation, 18f))
            }
        }

        gpsTracker = GpsTracker(applicationContext, object : GpsTracker.OnLocationUpdateListener {
            override fun onLocationUpdated(location: Location) {
                updatingNowLocation(location)
            }
        })

        startGameActivity(-10)

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