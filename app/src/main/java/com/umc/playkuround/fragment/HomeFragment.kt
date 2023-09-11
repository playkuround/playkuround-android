package com.umc.playkuround.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.umc.playkuround.R
import com.umc.playkuround.activity.AttendanceActivity
import com.umc.playkuround.activity.MainActivity
import com.umc.playkuround.activity.MajorChoiceActivity
import com.umc.playkuround.activity.MapActivity
import com.umc.playkuround.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), ActivityCompat.OnRequestPermissionsResultCallback {

    lateinit var binding : FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeCheckBlackBg.setOnClickListener {
            if(getLocationPermission()) {
                val intent = Intent(context, AttendanceActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(context, "위치 정보 접근 권한을 허용해주세요!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.homeAdventureBlackBg.setOnClickListener {
            if(getLocationPermission()) {
                val intent = Intent(context, MapActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(context, "위치 정보 접근 권한을 허용해주세요!", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun getLocationPermission() : Boolean {
        val pms1 = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
        val pms2 = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)

        if(pms1 == PackageManager.PERMISSION_GRANTED && pms2 == PackageManager.PERMISSION_GRANTED)
            return true
        else
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), 0)

        return false
    }



}