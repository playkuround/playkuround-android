package com.umc.playkuround.service

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Bundle
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.content.ContextCompat


class GpsTracker(private val mContext: Context) : Service(),
    LocationListener {

    private var mlocation : Location? = null
    private var latitude = 0.0
    private var longitude = 0.0

    private var locationManager: LocationManager? = null

    fun getLocation(mContext : Context): Location? {
        try {
            locationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager
            val isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGPSEnabled && !isNetworkEnabled) {
            } else {
                val hasFineLocationPermission = ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                    hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED
                ) {
                } else return null
                if (isNetworkEnabled) {
                    locationManager!!.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                        this
                    )
                    if (locationManager != null) {
                        mlocation =
                            locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        if (mlocation != null) {
                            latitude = mlocation!!.latitude
                            longitude = mlocation!!.longitude
                        }
                    }
                }
                if (isGPSEnabled) {
                    if (mlocation == null) {
                        locationManager!!.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                            this
                        )
                        if (locationManager != null) {
                            mlocation =
                                locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            if (mlocation != null) {
                                latitude = mlocation!!.latitude
                                longitude = mlocation!!.longitude
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("@@@", "" + e.toString())
        }
        return mlocation
    }

    fun getLatitude(): Double {
        if (mlocation != null) {
            Log.d("gps tracking", "getLatitude: if location != null")
            latitude = mlocation!!.latitude
        }
        return latitude
    }

    fun getLongitude(): Double {
        if (mlocation != null) {
            longitude = mlocation!!.longitude
        }
        return longitude
    }

    override fun onLocationChanged(location: Location) {
        Toast.makeText(mContext, "gps ${location.latitude}, ${location.longitude}", Toast.LENGTH_SHORT).show()
        mlocation = location
    }
    override fun onProviderDisabled(provider: String) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    fun stopUsingGPS() {
        if (locationManager != null) {
            locationManager!!.removeUpdates(this@GpsTracker)
        }
    }

    companion object {
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 1
        private const val MIN_TIME_BW_UPDATES = (10 * 30 * 1).toLong()
    }

    init {
        getLocation(mContext)
    }
}