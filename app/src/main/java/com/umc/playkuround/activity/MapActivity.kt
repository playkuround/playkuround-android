package com.umc.playkuround.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityMapBinding
import kotlin.random.Random


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

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
            .findFragmentById(R.id.map_map_fragment) as SupportMapFragment?
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

        val SEOUL = LatLng(37.56, 126.97)

        val markerOptions = MarkerOptions()
        markerOptions.position(SEOUL)
        markerOptions.title("서울")
        markerOptions.snippet("한국의 수도")
        map.addMarker(markerOptions)

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 18F))
        map.setPadding(0, screenHeight() - toPX(102) - toPX(40), screenWidth() - toPX(70), 0)
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

}