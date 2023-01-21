package com.umc.playkuround.data

data class Quiz (
    val landmarkId : Int,
    var question : String,
    var options : ArrayList<String>,
    var answer : Int
) {

    init {
        when(landmarkId) {
            1 -> {
                question = "다음 중 해당 층에 없는 시설은?"
                options = ArrayList()
                options.add(0, "1층 - 레스티오(카페)")
                options.add(1, "2층 - 상허교양대학 행정실")
                options.add(2, "3층 - 복사실") // 정답
                options.add(3, "4층 - 무용과 연습실")
                answer = 2
            }
            2 -> {

            }
            else -> {
                question = "정상적인 퀴즈가 아닙니다."
                options = ArrayList()
                options.add(0, "정답")
                options.add(1, "오답")
                options.add(2, "오답")
                options.add(3, "오답")
                answer = 0
            }
        }
    }

}
