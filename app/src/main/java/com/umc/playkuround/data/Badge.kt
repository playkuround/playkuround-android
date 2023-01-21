package com.umc.playkuround.data

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
        when(name) {
            ATTENDANCE_1 -> description = "처음으로 출석을 하셨군요!"

            else -> description = "알수없는 배지입니다."
        }
    }

}
