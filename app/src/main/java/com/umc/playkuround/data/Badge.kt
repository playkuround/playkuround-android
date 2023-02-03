package com.umc.playkuround.data

import com.umc.playkuround.R
import com.umc.playkuround.data.Badge.Companion.ATTENDANCE_30

data class Badge(
    val name : String,
    var description : String
) {

    companion object {
        const val ATTENDANCE_1 = "ATTENDANCE_1"
        const val ATTENDANCE_3 = "ATTENDANCE_3"
        const val ATTENDANCE_7 = "ATTENDANCE_7"
        const val ATTENDANCE_30 = "ATTENDANCE_30"
        const val ATTENDANCE_100 = "ATTENDANCE_100"
        const val ATTENDANCE_FOUNDATION_DAY = "ATTENDANCE_FOUNDATION_DAY"
        const val ADVENTURE_1 = "ADVENTURE_1"
        const val ADVENTURE_5 = "ADVENTURE_5"
        const val ADVENTURE_10 = "ADVENTURE_10 "
        const val ADVENTURE_30 = "ADVENTURE_30"
        const val ENGINEER = "ENGINEER"
        const val ARTIST = "ARTIST"
        const val CEO = "CEO"
        const val NATIONAL_PLAYER = "NATIONAL_PLAYER"
        const val CONQUEROR = "CONQUEROR"
        const val NEIL_ARMSTRONG = "NEIL_ARMSTRONG"
    }

    init {
        description = when(name) {
            ATTENDANCE_1 -> "처음으로 출석을 하셨군요!"
            ATTENDANCE_3 -> "3일 연속 출석이에요!"
            ATTENDANCE_7 -> "7일 연속 출석이에요!"
            ATTENDANCE_30 -> "30일 연속 출석이에요!"
            ATTENDANCE_100 -> "우와! 100일 연속 출석이에요!"
            ATTENDANCE_FOUNDATION_DAY -> "당신을 애교자로 임명합니다. 애교(校)자!"
            ADVENTURE_1 -> "첫 탐험이에요"
            ADVENTURE_5 -> "다섯번째 탐험이에요"
            ADVENTURE_10 -> "영차! 열번째 탐험이에요"
            ADVENTURE_30 -> "우와.. 30번째 탐험이에요!"
            ENGINEER -> "당신은 공대생..? 공학관을 모두 탐험했어요"
            ARTIST -> "ARTIST! 예디대 공예관을 모두 탐험했어요"
            CEO -> "경영대와 문과 건물을 모두 탐험했어요"
            NATIONAL_PLAYER -> "헛둘헛둘! 체육시설을 모두 탐험한 당신은 국가대표..?"
            CONQUEROR -> "랜드마크를 모두 정복했어요!"
            NEIL_ARMSTRONG -> "학교의 문을 모두 정복한 당신은 건국대의 닐 암스트롱..?"


            else -> "알수없는 배지입니다."
        }
    }

    fun getImageDrawable() : Int {
        return when(name) {
            ATTENDANCE_1 -> R.drawable.badge_attendance_1
            ATTENDANCE_3 -> R.drawable.badge_attendance_3
            ATTENDANCE_7 -> R.drawable.badge_attendance_7
            ATTENDANCE_30 -> R.drawable.badge_attendance_30
            ATTENDANCE_100 -> R.drawable.badge_attendance_100
            ATTENDANCE_FOUNDATION_DAY -> R.drawable.badge_attendance_foundation_day
            ADVENTURE_1 -> R.drawable.badge_adventure_1
            ADVENTURE_5 -> R.drawable.badge_adventure_5
            ADVENTURE_10 -> R.drawable.badge_adventure_10
            ADVENTURE_30 -> R.drawable.badge_adventure_30
            ENGINEER -> R.drawable.badge_engineer
            ARTIST -> R.drawable.badge_artist
            CEO -> R.drawable.badge_ceo
            NATIONAL_PLAYER -> R.drawable.badge_national_player
            CONQUEROR -> R.drawable.badge_conqueror
            NEIL_ARMSTRONG -> R.drawable.badge_neil_armstrong

            else -> R.color.lighter_gray
        }
    }

    fun getTitle() : String {
        return when(name) {
            ATTENDANCE_1 -> "첫 출석"
            ATTENDANCE_3 -> "3일 연속 출석"
            ATTENDANCE_7 -> "7일 연속 출석"
            ATTENDANCE_30 -> "30일 연속 출석"
            ATTENDANCE_100 -> "100일 연속 출석"
            ATTENDANCE_FOUNDATION_DAY -> "애교(校)자"
            ADVENTURE_1 -> "첫 탐험"
            ADVENTURE_5 -> "오탐험"
            ADVENTURE_10 -> "십탐험"
            ADVENTURE_30 -> "삼십탐험"
            ENGINEER -> "공대생"
            ARTIST -> "예술가"
            CEO -> "CEO"
            NATIONAL_PLAYER -> "국가대표"
            CONQUEROR -> "랜드마크 정복자"
            NEIL_ARMSTRONG -> "닐 암스트롱"

            else -> "없는 배지"
        }
    }

}
