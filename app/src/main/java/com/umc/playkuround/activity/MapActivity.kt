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
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.umc.playkuround.PlayKuApplication.Companion.user
import com.umc.playkuround.R
import com.umc.playkuround.data.LandMark
import com.umc.playkuround.data.Ranking
import com.umc.playkuround.databinding.ActivityMapBinding
import com.umc.playkuround.dialog.LoadingDialog
import com.umc.playkuround.dialog.SmallSlideUpDialog
import com.umc.playkuround.service.GpsTracker
import com.umc.playkuround.service.UserService
import java.util.*
import kotlin.concurrent.timer


class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    lateinit var binding : ActivityMapBinding
    private lateinit var map : GoogleMap
    private lateinit var locationCallback : LocationCallback
    private lateinit var visitedList : ArrayList<LandMark>

    private var myLocation : Marker? = null
    private var nowLocation : LatLng = LatLng(0.0, 0.0)
    private lateinit var gpsTracker : GpsTracker
    private var timer : Timer? = null

    lateinit var loadingDialog : LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mapBackBtn.setOnClickListener{
            finish()
        }

        loadingDialog = LoadingDialog(this)
        loadingDialog.show()
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
        val lat = location.getLatitude()
        val lon = location.getLongitude()
        loadingDialog.dismiss()
        //Log.d("updating now location", "updatingNowLocation: lat : $lat, lon : $lon")

        val markerOptions = MarkerOptions()
        nowLocation = LatLng(lat, lon)

        markerOptions.position(nowLocation)
        markerOptions.snippet("19")

        runOnUiThread {
            myLocation = if(myLocation != null) {
                myLocation!!.remove()
                map.addMarker(markerOptions)
            } else {
                map.addMarker(markerOptions)
            }
        //Toast.makeText(applicationContext, "$lat, $lon", Toast.LENGTH_SHORT).show()
        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(nowLocation!!, 17f))
        }
    }

    private fun startGameActivity(idx : Int) {
        binding.mapClickBtn.setOnClickListener {
            gpsTracker.requestLastLocation()
            val loading = LoadingDialog(this)
            loading.show()

            val userService = UserService()
            userService.setOnResponseListener(object : UserService.OnResponseListener() {
                override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                    if(isSuccess) {
                        loading.dismiss()
                        Log.d("near_landmark", "getResponseBody: $body")
                        val landmark = body as LandMark
                        Log.d("map activity check", "getResponseBody: $landmark")
                        Log.d("map activity check", "getResponseBody: $visitedList")

                        if(visitedList.contains(LandMark(landmark.id, 0.0, 0.0, "", 0.0, ""))) {
                            saveAdventureLog(landmark)
                        } else {
                            val intent: Intent = when (landmark.gameType) {
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
                        }
                    } else {
                        loading.dismiss()
                        Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
                    }
                }
            }).getNearLandmark(user.getAccessToken(), nowLocation.latitude.toString(), nowLocation.longitude.toString())
            Log.d("xdxd", "startGameActivity: " + nowLocation.latitude.toString() + "/" + nowLocation.longitude.toString())
        }

        if(idx == -1) {
            binding.mapClickBtn.setOnClickListener {
                //val intent = Intent(applicationContext, MiniGameQuizActivity::class.java)
                val intent = Intent(applicationContext, MiniGameMoonActivity::class.java)
                //val intent = Intent(applicationContext, MiniGameTimerActivity::class.java)
                intent.putExtra("landmark", LandMark(1, 123.2131, 321.1234, "", 0.0, ""))
                startActivity(intent)
            }
        }
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
        map.setLatLngBoundsForCameraTarget(kuBound)

        map.uiSettings.isCompassEnabled = false
        map.uiSettings.isMapToolbarEnabled = false

        setMarker()
        map.setOnMarkerClickListener(this)
    }

    private fun setMarker() {
        val loading = LoadingDialog(this)
        loading.show()

        val userService = UserService()
        userService.setOnResponseListener(object : UserService.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                if(isSuccess) {
                    loading.dismiss()
                    Log.d("getUserAdventureLog", "getResponseBody: $body")

                    visitedList = body as ArrayList<LandMark>

                    visitedList.forEach { l ->
                        val landmark = LatLng(l.latitude, l.longitude)

                        val bitmapDraw = ResourcesCompat.getDrawable(resources, R.drawable.img_flag, null) as BitmapDrawable
                        val b = bitmapDraw.bitmap
                        val smallMarker = Bitmap.createScaledBitmap(b, 60, 90, false)

                        val markerOptions = MarkerOptions()

                        markerOptions.position(landmark)
                        markerOptions.title(l.name)
                        markerOptions.snippet("${l.id}")
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))

                        map.addMarker(markerOptions)
                    }
                } else {
                    loading.dismiss()
                    Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
                }
            }
        }).getUserAdventureLog(user.getAccessToken())
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

        gpsTracker = GpsTracker(applicationContext, object : GpsTracker.OnLocationUpdateListener {
            override fun onLocationUpdated(location: Location) {
                updatingNowLocation(location)
            }
        })

        startGameActivity(-10)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_map_fragment) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        MapsInitializer.initialize(this)

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
        //p0.showInfoWindow()
        placeinfoDialog(p0.snippet!!.toInt())
        return true
    }

    private fun placeinfoDialog(id : Int) {
        val landmark = LandMark(id, 0.0, 0.0, "", 0.0, "")

        val contentView: View =
            (this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.dialog_map_place, null
            )

        val slideupPopup = SmallSlideUpDialog.Builder(this)
            .setContentView(contentView)
            .create()

        val maprankBtn = slideupPopup.findViewById<Button>(R.id.map_place_rank_bt)
        maprankBtn.setOnClickListener {
            val intent = Intent(applicationContext,DialogPlaceRankActivity::class.java)
            intent.putExtra("landmark", landmark)
            startActivity(intent)
        }

        val mapinfoBtn = slideupPopup.findViewById<Button>(R.id.map_place_info_bt)
        mapinfoBtn.setOnClickListener {
            val intent = Intent(applicationContext,DialogPlaceInfoActivity::class.java)
            intent.putExtra("landmark", landmark)
            startActivity(intent)
        }

        val title = slideupPopup.findViewById<TextView>(R.id.map_place_title_tv)
        title.text = landmark.name

        val mapImg = slideupPopup.findViewById<ImageView>(R.id.map_place_iv)
        mapImg.setImageResource(landmark.getImageDrawable())

        if(this.isFinishing) Log.d("location dialog", "placeinfoDialog: finishing")
        else slideupPopup.show()
    }

}