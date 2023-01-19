package com.umc.playkuround.data

import com.google.gson.annotations.SerializedName
import com.umc.playkuround.R
import java.io.Serializable

data class LandMark(
    @SerializedName(value = "landmarkId") val id : Int,
    @SerializedName(value = "latitude") var latitude : Double,
    @SerializedName(value = "longitude") var longitude : Double,
    var name : String,
    val distance : Double,
    val gameType : String
) : Serializable {

    companion object {
        const val QUIZ = "QUIZ"
        const val TIMER = "TIMER"
        const val MOON = "MOON"
    }

    init {
        when(this.id) {
            1 -> {
                this.name = "산학협동관"
                this.latitude = 37.5399272
                this.longitude = 127.0730058
            }
            2 -> {
                
            }
            else -> {
                this.name = "존재하지 않는 건물"
                this.latitude = 0.0
                this.longitude = 0.0
            }
        }
    }

    fun getDescription() : String {
        return when(this.id) {
            1 -> "대형강의실이 많으며 1층에는 건국유업 카페 브랜드인 레스티오가 있습니다. " +
                    "상허교양대학 행정실이 있어 교양 과목 추가신청과 성적 이의제기를 할 수 있습니다. " +
                    "1층 마당에 따릉이 대여소가 있어 유용하게 사용할 수 있고 2층에는 강의실과 복사실이 있습니다."
            2 -> ""
            3 -> ""
            else -> "존재하지 않는 건물입니다."
        }
    }

    fun getImageDrawable() : Int {
        return when(this.id) {
            1 -> R.drawable.landmark_1
            2 -> 0
            3 -> 0
            else -> R.color.lighter_gray
        }
    }

}
