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
import android.os.Bundle
import android.telephony.CarrierConfigManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityMapBinding
import kotlin.random.Random


class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationListener {

    lateinit var binding : ActivityMapBinding
    private lateinit var map : GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mapBackBtn.setOnClickListener{
            finish()
        }

        startGameActivity(getGames())

        val mapFragment = supportFragmentManager
            .findFragmentById(com.umc.playkuround.R.id.map_map_fragment) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    private fun startGameActivity(idx : Int) {
        binding.mapClickBtn.setOnClickListener {
            val intent : Intent = when (idx) {
                0 -> {
                    Intent(this, MiniGameQuizActivity::class.java)
                }
                1 -> {
                    Intent(this, MiniGameMoonActivity::class.java)
                }
                2 -> {
                    Intent(this, MiniGameTimerActivity::class.java)
                }
                else -> {
                    Intent(this, MiniGameTimerActivity::class.java)
                }
            }
            startActivity(intent)
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
        val locationManager : LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("gps", "onResume: start gps")
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10f, this)
        }
        super.onResume()
    }

    /*private fun getNowLocation() : Pair<Double, Double> {
        val locationManager : LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        var location : Location? = if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10f, this)
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 0
            )
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10f, this)
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        }

        return if(location != null) {
            val lat = location.latitude
            val lng = location.longitude
            Pair(lat, lng)
        } else
            Pair(37.5399272, 127.0730058)
    }*/

    override fun onMarkerClick(p0: Marker): Boolean {
        p0.showInfoWindow()
        return true
    }

    override fun onLocationChanged(p0: Location) {
        val nowLocation = LatLng(p0.latitude, p0.longitude)
        Log.d("gps", "onLocationChanged: ${p0.latitude} , ${p0.longitude}")
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(nowLocation, 18f))
    }

}