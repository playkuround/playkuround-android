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
                question = "다음 중 입학처 2023 학년도 수시 학생부종합전형 모집요강에 해당하지 않는 것은?"
                options = ArrayList()
                options.add(0, "KU 자기 추천")
                options.add(1, "특성화 고졸 재직자")
                options.add(2, "농어촌 학생")
                options.add(3, "KU 지역 균형") // 정답
                answer = 3
            }

            3 -> {
                question = "다음 중 건국대학교 수의과대학에 맞는 정보는?"
                options = ArrayList()
                options.add(0, "건국대학교 수의과대학은 총 4년제이다.")
                options.add(1, "2022년 기준, 전국의 수의과대학 중 유일한 사립대학교이다.") // 정답
                options.add(2, "수의학과는 2022년 기준 총 12개 대학교에 개설되어 있다.")
                options.add(3, "수의과대학 졸업 기준 최소 이수 학점은 250 학점이다.")
                answer = 1

            }

            4 -> {
                question = "다음 중 학과 사무실이 동물생명과학관에 없는 학과는?"
                options = ArrayList()
                options.add(0, "식품유통공학과")
                options.add(1, "융합생명공학과")// 정답
                options.add(2, "축산식품생명공학과")
                options.add(3, "동물자원과학과")
                answer = 1

            }

            5 -> {
                question = "다음 중 사무실이 생명과학관에 없는 학과는?"
                options = ArrayList()
                options.add(0, "유전생명공학과") // 정답
                options.add(1, "화장품공학과")
                options.add(2, "스마트운행체공학과")
                options.add(3, "시스템생명공학과")
                answer = 0

            }

            6 -> {
                question = "상허도서관 내 복사실이 위치한 층은?"
                options = ArrayList()
                options.add(0, "1층") // 정답
                options.add(1, "2층")
                options.add(2, "3층")
                options.add(3, "4층")
                answer = 0

            }


            7 -> {
                question = "의생명과학연구관은 최대 지하 층수는?"
                options = ArrayList()
                options.add(0, "지하 1층")
                options.add(1, "지하 2층")
                options.add(2, "지하 3층")
                options.add(3, "지하 4층")// 정답
                answer = 3

            }

            8 -> {
                question = "예술디자인대학 소속 학과가 아닌 것은?"
                options = ArrayList()
                options.add(0, "산업디자인")
                options.add(1, "의상디자인")
                options.add(2, "영상영화")
                options.add(3, "시각디자인")// 정답
                answer = 3

            }

            9 -> {
                question = "언어교육원은 과거에 다른 건물로 쓰였다. 쓰였던 건물은?"
                options = ArrayList()
                options.add(0, "공학관")
                options.add(1, "병원")
                options.add(2, "중앙도서관") // 정답
                options.add(3, "학생회관")
                answer = 2

            }

            10 -> {
                question = "건국대학교 로스쿨 창립연도는?"
                options = ArrayList()
                options.add(0, "2006년")
                options.add(1, "2007년")
                options.add(2, "2008년") // 정답
                options.add(3, "2009년")
                answer = 2

            }

            11 -> {
                question = "상허박물관에서 찍었던 드라마 이름은?"
                options = ArrayList()
                options.add(0, "유미의 세포들")
                options.add(1, "도깨비")
                options.add(2, "치즈인더트랩") // 정답
                options.add(3, "더킹:영원의 군주")
                answer = 2

            }

            12 -> {
                question = "행정관에 있는 ATM 기기로 옳은 것은?"
                options = ArrayList()
                options.add(0, "우리은행")
                options.add(1, "국민은행") // 정답
                options.add(2, "농협은행")
                options.add(3, "IBK은행")
                answer = 1

            }

            13 -> {
                question = "교육과학관 남녀 화장실이 둘다 있는 층수는?"
                options = ArrayList()
                options.add(0, "1층") // 정답
                options.add(1, "2층")
                options.add(2, "3층")
                options.add(3, "4층")
                answer = 0

            }

            14 -> {
                question = "사회과학대학을 통합할 때 속한 대학으로 옳지 않은 것은?"
                options = ArrayList()
                options.add(0, "정치대학")
                options.add(1, "상경대학")
                options.add(2, "부동산대학") // 정답
                options.add(3, "글로벌융합대학")
                answer = 2

            }

            16 -> {
                question = "새천년관에 대해 옳지 않은 것은?"
                options = ArrayList()
                options.add(0, "무엇이든 물어보살에 나온 적이 있다.")
                options.add(1, "건국대학교 건축대학교수님이 직접 설계했다.")
                options.add(2, "대공연장이 존재한다.")
                options.add(3, "어린이대공원역보다 건대입구역이 가깝다.") // 정답
                answer = 3

            }

            17 -> {
                question = "건축대학에 대해 옳지 않은 것은?"
                options = ArrayList()
                options.add(0, "건축대학 학생은 CAD실의 컴퓨터를 이용할 수 있다.")
                options.add(1, "건축학과와 건축공학과가 통합되어 있다.")
                options.add(2, "건축대학 건물은 17번이다.")
                options.add(3, "건축대학교는 5학년까지 다닌다.") // 정답
                answer = 3

            }

            18 -> {
                question = "부동산학관에 대한 내용 중 틀린 것은?"
                options = ArrayList()
                options.add(0, "부동산학관 건물은 2015년에 완공되었다.")
                options.add(1, "기업연계형 장기현장실습(IPP) 프로그램을 운영중이다.")
                options.add(2, "건국 PRIDE Leading Group(선도학과)로 선정된 적이 있다.")
                options.add(3, "야간석사과정과 야간박사과정이 존재한다") // 정답
                answer = 3

            }

            19 -> {
                question = "인문학관에 대해 옳지 않은 것은?"
                options = ArrayList()
                options.add(0, "최근 문과대학에 일어일문학과가 개설되었다.") // 정답
                options.add(1, "2017년 중앙일보 대학평가 결과 인문계열 5위를 차지했다.")
                options.add(2, "인문학관에는 여자휴게실이 존재한다.")
                options.add(3, "미디어커뮤니케이션학과는 문과대학에 속한다.")
                answer = 0

            }

            21 -> {
                question = "제2학생회에 대해 옳지 않은 것은?"
                options = ArrayList()
                options.add(0, "동아리 방들이 모여있다.")
                options.add(1, "모든 중앙동아리는 동아리방이 있다.") // 정답
                options.add(2, "지하와 2층에 샤워실이 있다.")
                options.add(3, "축제를 할 때 제일 사람이 많다.")
                answer = 1

            }

            23 -> {
                question = "공학관 B동과 관련 없는 것은?"
                options = ArrayList()
                options.add(0, "168호에서 소프트웨어를 대여할 수 있다.")
                options.add(1, "공학관 A동과 연결되어 있다.")
                options.add(2, "학생회관에서 공학관을 바라보는 기준으로 왼쪽 건물이다.")
                options.add(3, "이과대학 건물과 다리로 연결되어 있다.") // 정답
                answer = 3

            }

            24 -> {
                question = "공학관C동의 마지막 강의실은?"
                options = ArrayList()
                options.add(0, "424호")
                options.add(1, "456호")
                options.add(2, "483호")
                options.add(3, "492호") // 정답
                answer = 3

            }

            25 -> {
                question = "신공학관의 최대 층수는?"
                options = ArrayList()
                options.add(0, "10층")
                options.add(1, "11층")
                options.add(2, "12층") // 정답
                options.add(3, "13층")
                answer = 2

            }

            27 -> {
                question = "창의관이 이전에 사용된 용도는?"
                options = ArrayList()
                options.add(0, "건대부중")// 정답
                options.add(1, "건대부고")
                options.add(2, "과학관")
                options.add(3, "농업대")
                answer = 0

            }

            28 -> {
                question = "다음 중 리빙디자인학과의 전공 수업이 아닌 것은?"
                options = ArrayList()
                options.add(0, "물레조형2")
                options.add(1, "건축도자")
                options.add(2, "염색")
                options.add(3, "스페이스플래닝3 ") // 정답
                answer = 3

            }

            29 -> {
                question = "2022년 2학기에 교환학생으로 갈 수 없었던 나라는?"
                options = ArrayList()
                options.add(0, "그레나다")
                options.add(1, "아이슬란드")
                options.add(2, "뉴질랜드")// 정답
                options.add(3, "노르웨이")
                answer = 2

            }

            31 -> {
                question = "일감호 면적 안에 들어가는 대학교는?"
                options = ArrayList()
                options.add(0, "한국외국어대학교(서울캠)")
                options.add(1, "광운대학교")
                options.add(2, "세종대학교")
                options.add(3, "한성대학교") // 정답
                answer = 3

            }

            32 -> {
                question = "홍예교의 전설로 옳은 것은?"
                options = ArrayList()
                options.add(0, "썸탈때 건너면 사귀고 커플 때 건너면 헤어진다.") // 정답
                options.add(1, "시험기간에 건너면 다음 시험 망친다.")
                options.add(2, "새벽 4시에 다리에서 물을 보면 귀신을 볼 수 있다.")
                options.add(3, "3명 이상 건너면 저주를 받는다.")
                answer = 0

            }




            else -> {
                question = "잘못된 퀴즈입니다."
                options = ArrayList()
                options.add(0, "") // 정답
                options.add(1, "")
                options.add(2, "")
                options.add(3, "")
                answer = 0
            }
        }
    }

}
