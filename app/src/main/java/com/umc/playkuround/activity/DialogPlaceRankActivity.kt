package com.umc.playkuround.activity

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.umc.playkuround.PlayKuApplication
import com.umc.playkuround.PlayKuApplication.Companion.user
import com.umc.playkuround.R
import com.umc.playkuround.data.LandMark
import com.umc.playkuround.databinding.DialogPlaceRankBinding
import com.umc.playkuround.dialog.LoadingDialog
import com.umc.playkuround.service.UserService

class DialogPlaceRankActivity : AppCompatActivity() {

    lateinit var binding : DialogPlaceRankBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogPlaceRankBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        val loading = LoadingDialog(this)
        loading.show()

        val landmark = intent.getSerializableExtra("landmark") as LandMark

        val userService = UserService()
        userService.setOnResponseListener(object : UserService.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                if(isSuccess) {
                    val list = body as ArrayList<HashMap<String, String>>
                    binding.dialogGetBadgePlaceTv.text = landmark.name

                    binding.rankingMyScoreTv.text = list[0]["count"]
                    binding.rankingMyRankTv.text = list[0]["ranking"]

                    binding.dialogGetBadgeNameTv.text = list[1]["nickname"]
                    binding.dialogGetBadgePlaceCountTv.text = "+ " + list[1]["count"].toString()
                    binding.dialogGetBadgeRankNameTv.text = list[1]["nickname"]
                    binding.dialogGetRankScoreTv.text = list[1]["count"]

                    if(list.size > 2) {
                        binding.dialogGetBadgeMyBar2.visibility = View.VISIBLE
                        binding.dialogGetBadgeRankName2Tv.text = list[2]["nickname"]
                        binding.dialogGetRankScore2Tv.text = list[2]["count"]
                    } else if(list.size > 3) {
                        binding.dialogGetBadgeMyBar3.visibility = View.VISIBLE
                        binding.dialogGetBadgeRankName3Tv.text = list[3]["nickname"]
                        binding.dialogGetRankScore3Tv.text = list[3]["count"]
                    } else if(list.size > 4) {
                        binding.dialogGetBadgeMyBar4.visibility = View.VISIBLE
                        binding.dialogGetBadgeRankName4Tv.text = list[4]["nickname"]
                        binding.dialogGetRankScore4Tv.text = list[4]["count"]
                    } else if(list.size > 5) {
                        binding.dialogGetBadgeMyBar5.visibility = View.VISIBLE
                        binding.dialogGetBadgeRankName5Tv.text = list[5]["nickname"]
                        binding.dialogGetRankScore5Tv.text = list[5]["count"]
                    }

                    loading.dismiss()
                } else {
                    loading.dismiss()
                    Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
                }
            }
        }).getPlaceRank(user.getAccessToken(), landmark.id)
    }

}