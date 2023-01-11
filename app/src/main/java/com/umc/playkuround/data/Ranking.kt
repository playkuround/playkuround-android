package com.umc.playkuround.data

data class Ranking(
    val ranking : Int,
    val nickname : String,
    val points : Int
) {
    companion object {
        class ScoreType() {
            val ATTENDANCE = "ATTENDANCE"
            val ADVENTURE = "ADVENTURE"
            val EXTRA_ADVENTURE = "EXTRA_ADVENTURE"
        }

        val scoreType = ScoreType()
    }
}
