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
                this.latitude = 37.539765
                this.longitude = 127.073215
            }
            2 -> {
                this.name = "입학정보관"
                this.latitude = 37.540296
                this.longitude = 127.073410
                
            }
            3 -> {
                this.name = "수의학관"
                this.latitude = 37.539310
                this.longitude = 127.074590
            }

            4 -> {
                this.name = "동물생명과학관"
                this.latitude = 37.540184
                this.longitude = 127.074179
            }

            5 -> {
                this.name = "생명과학관"
                this.latitude = 37.540901
                this.longitude = 127.074055

            }

            6 -> {
                this.name = "상허도서관"
                this.latitude = 37.542051
                this.longitude = 127.073808
            }

            7 -> {
                this.name = "의생명과학연구관"
                this.latitude = 37.541461
                this.longitude = 127.072215
            }

            8 -> {
                this.name = "예디대"
                this.latitude = 37.542908
                this.longitude = 127.072815
            }

            9 -> {
                this.name = "언어교육원"
                this.latitude = 37.542533
                this.longitude = 127.074700

            }

            10 -> {
                this.name = "법학관"
                this.latitude = 37.541667
                this.longitude = 127.075046
            }

            11 -> {
                this.name = "상허박물관"
                this.latitude = 37.542375
                this.longitude = 127.075711

            }

            12 -> {
                this.name = "행정관"
                this.latitude = 37.543222
                this.longitude = 127.075305
            }

            13 -> {
                this.name = "교육과학관"
                this.latitude = 37.543998
                this.longitude = 127.074145
            }

            14 -> {
                this.name = "상허연구관"
                this.latitude = 37.544018
                this.longitude = 127.075141

            }

            15 -> {
                this.name = "경영관"
                this.latitude = 37.544319
                this.longitude = 127.076184
            }

            16 -> {
                this.name = "새천년관"
                this.latitude = 37.543374
                this.longitude = 127.077314
            }

            17 -> {
                this.name = "건축관"
                this.latitude = 37.543732
                this.longitude = 127.078482
            }

            18 -> {
                this.name = "부동산학관"
                this.latitude = 37.543283
                this.longitude = 127.078093
            }

            19 -> {
                this.name = "인문학관"
                this.latitude = 37.542602
                this.longitude = 127.078250

            }

            20 -> {
                this.name = "학생회관"
                this.latitude = 37.541954
                this.longitude = 127.077748
            }

            21 -> {
                this.name = "제2학생회관"
                this.latitude = 37.541298
                this.longitude = 127.077828
            }

            22 -> {
                this.name = "공학관A"
                this.latitude = 37.541655
                this.longitude = 127.078769
            }

            23 -> {
                this.name = "공학관B"
                this.latitude = 37.542004
                this.longitude = 127.079563
            }

            24 -> {
                this.name = "공학관C"
                this.latitude = 37.541226
                this.longitude = 127.079357

            }

            25 -> {
                this.name = "신공학관"
                this.latitude = 37.540455
                this.longitude = 127.079304
            }

            26 -> {
                this.name = "과학관(이과대)"
                this.latitude = 37.541491
                this.longitude = 127.080565
            }

            27 -> {
                this.name = "창의관"
                this.latitude = 37.541114
                this.longitude = 127.081743

            }

            28 -> {
                this.name = "공예관"
                this.latitude = 37.542220
                this.longitude = 127.080961
            }

            29 -> {
                this.name = "KU기술혁신관"
                this.latitude = 37.539995
                this.longitude = 127.077180
            }

            30 -> {
                this.name = "기숙사"
                this.latitude = 37.539147
                this.longitude = 127.078248

            }

            31 -> {
                this.name = "일감호"
                this.latitude = 37.540796
                this.longitude = 127.076495
            }

            32 -> {
                this.name = "홍예교"
                this.latitude = 37.541688
                this.longitude = 127.077344
            }

            33 -> {
                this.name = "황소동상"
                this.latitude = 37.543135
                this.longitude = 127.076172
            }

            34 -> {
                this.name = "청심대"
                this.latitude = 37.542366
                this.longitude = 127.076835
            }

            35 -> {
                this.name = "상허박사 동상"
                this.latitude = 37.541365
                this.longitude = 127.073457
            }

/*            36 -> {
                this.name = "노천극장 중앙"
                this.latitude = 37.5414752
                this.longitude = 127.0778020
            }*/

            37 -> {
                this.name = "운동장"
                this.latitude = 37.544387
                this.longitude = 127.077593
            }

            38 -> {
                this.name = "실내체육관"
                this.latitude = 37.544456
                this.longitude = 127.079587
            }

            39 -> {
                this.name = "건국문(후문)"
                this.latitude = 37.545122
                this.longitude = 127.076613
            }

            40 -> {
                this.name = "중문"
                this.latitude = 37.541827
                this.longitude = 127.071780
            }

            41 -> {
                this.name = "일감문"
                this.latitude = 37.539165
                this.longitude = 127.074042
            }

            42 -> {
                this.name = "상허문"
                this.latitude = 37.539692
                this.longitude = 127.072491
            }

            43 -> {
                this.name = "구의역 쪽문"
                this.latitude = 37.541584
                this.longitude = 127.082226
            }

            44 -> {
                this.name = "기숙사 쪽문"
                this.latitude = 37.539255
                this.longitude = 127.076741
            }

            else -> {
                this.name = "존재하지 않는 건물"
                this.latitude = 37.5399272
                this.longitude = 127.0730058
            }
        }
    }

    fun getDescription() : String {
        return when(this.id) {
            1 -> "대형강의실이 많으며 1층에는 건국유업 카페 브랜드인 레스티오가 있어요. " +
                    "상허교양대학 행정실이 있어 교양 과목 추가 신청과 성적 이의 제기를 할 수 있어요. " +
                    "1층 마당에 따릉이 대여소가 있어 유용하게 사용할 수 있고 2층에는 강의실과 복사실이 있어요."
            2 -> "입학처와 교내 벤처기업, 정보통신처, PC 클리닉이 입주해있어요. " +
                    "1층 정보통신처에 학생증을 제시하면 마이크로스트 오피스, 한글 등 여러 소프트웨어들을 대여할 수 있어요."
            3 -> "수의과대학과 동물병원이 위치해있어요. " +
                    "학교 정문(일감문) 바로 옆에 위치해있어요. " +
                    "운이 좋으면 수의학과에서 키우는 강아지 산책을 볼 수 있어요."
            4 -> "1층에 레스티오와 K-CUBE가 위치해있어요. " +
                    "상허생명과학대학 학부생들이 주로 사용하는 건물이에요. "
            5 -> "건물 2층에 K-CUBE가 위치해있어요. " +
                    "KU 융합과학기술원 소속 학과들이 주로 이용해요. " +
                            "농축대학원이 위치해있어요."
            6 -> "1,4,5층은 자료실 3, 6층은 열람실로 사용돼요. " +
                    "열람실이 있는 3층 출입구에는 시험기간에 허기진 배를 채울 수 있는 CU가 있어요. " +
                    "기존의 6열람실과 국가고시연구실 없애고 K-Cube를 만들었어요. " +
                    "상허문에서 상허기념도서관에 이르는 도로 양편에 세계의 언어와 문자를 새긴 비석이 세워져 있어요."
            7 -> "건국대학교 글로벌캠퍼스 소속인 의학전문대학원 및 특성화학부가 수업을 듣는 곳이에요. 건대 병원은 재학생 제휴 할인이 가능해요."
            8 -> "카페 레스티오가 있으며 예술영화관인 ku시네마가 자리 잡고 있어요. " +
                    "3층에는 테라스 형식으로 된 휴게소가 있으며 주로 흡연실로 사용한다 해요.(카더라) " +
                    "건국대학교 학생이라면 평일 7000원, 주말 9000원에 영화를 볼 수 있어요."
            9 -> "과거 도서관이었어요. " +
                    "방학 중 개강하는 토익 토플 수업은 대부분 언어교육원에서 진행해요. " +
                    "언어교육원에서 진행하는 전화영어가 꽁짜고 꿀이라는 소문이 있어요."
            10 -> "건국대 로스쿨이 존재해요. " +
                    "시간표에 종강이라 표시돼요. 법학관이 종합강의동이라 표시되지않아 혼란스러운 새내기들은 참고하세요! " +
                    "1층에는 법학전문도서관이 있어요."
            11 -> "상허박물관에는 역사 유물 뿐만 아니라 우리 대학의 설립자인 상허 유석창의 유품과 학교의 역사 자료를 볼 수 있어요."
            12 -> "학생회관 학생복지처에서 발급받을 수 없는 수입인지의 경우 여기에서 발급받을 수 있어요."
            13 -> "사범대학학생들이 많이 가며 타 학과의 학생인 경우에도 교직이수를 할 때 교과교육론이나 교재연구론을 듣기 위해 가요."
            14 -> "1층에는 블루포트라는 카페가 있으며 3층은 학부강의실과 K큐브(오픈형 자습공간)이 있어요. 특히 스터디룸이 있어 팀플하기 좋으며 위인전을 통해 예약을 할 수 있어요."
            15 -> "2021년에 신식으로 1층에 K-HUB(오픈형 자습공간), 편의점이 있어요. " +
                    "상허연구관이 생기기 전에는 “사회과학관”이라는 이름이였으며 정치대-행정학과, 정외과, 부동산학과가 있었어요. " +
                    "웹툰 치즈인더트랩의 배경이에요."
            16 -> "건국대학교의 기초교양인 sw교양인 컴퓨팅적사고, 프로그래밍을 통한 문제해결을 듣기 위해 많이 가는 건물이에요. " +
                    "새천년관 1층에 화장실이 없으니 급하면 다른 곳을 찾아보세요. " +
                    "지하 1층에 교직원 식당이 있어요."

            17 -> "설계 스튜디오와 CAD 실습실, K-Hub 존재해요."
            18 -> "15년, 16년에 신축되었어요. " +
                    "기부해주신 성함으로 강의실이 마련되어 있어요. " +
                    "세미나실과 휴게실, CEO 라운지, 카페 있어요."
            19 -> "문과대학이 위치해있어요. " +
                    "1950년대 지어졌어서 굉장히 오래됐어요."
            20 -> "학식을 먹을 수 있어요. " +
                    "신한은행이 있어요. " +
                    "고전음악감상실이 있어 공강 시간에 쉬어갈 수 있어요. " +
                    "2층에서 학생증을 재발급 받을 수 있어요."
            21 -> "주로 동아리실이 모여있어요. " + " 축제 공연을 할때 노천극장에서 주로 진행해요."
            22 -> "엘레베이터가 없어요. " +
                    "1층에 서점이 위치해 있어요. " +
                    "공대 과사무실들이 위치해 있어요."
            23 -> "168호에서 소프트웨어를 무료로 대여할 수 있어요. 이때 학생증 및 신분증은 필수에요!"
            24 -> "전기전자공학부, 사회환경공학부, 컴퓨터공학부 교수님들이 있어요."
            25 -> "2016년에 완공됐어요. " +
                    "건대에서 가장 최근에 지어진 건물이에요. " +
                    "12개 층이 있어요. 엘리베이터가 4개에요. 수업 시작 전에는 사람이 몰리므로 수업 시작 15분 전에는 1층에 도착해야 안정빵이에요! " +
                    "KU 스마트 팩토리가 있고, 3D 프린터가 있어요. " +
                    "대부분의 공대 강의실이 있어요. " +
                    "공대학원생들이 상주하고 있어요. " +
                    "1층에 자유롭게 공부할 수 있는 장소가 있어요."
            26 -> "건물 이름은 과학관이지만 보통 이과대라고 불러요. " +
                    "이과대학생들이 사용해요.(수학과, 화학과, 물리학과) " +
                    "공대생들은 보통 1학년 물리, 화학 실험 수업을 들으러 이용해요. " +
                    "화학실험의 경우 실험복이 없으면 입장이 불가능해요. " +
                    "4층(5층)인가에 실험레포트를 제출하는 우편함이 있어요."
            27 -> "신산업융학과 교수님들의 연구실이 있어요. " +
                    "학군단이 있어요. " +
                    "구의역에서 가는게 빨라요. " +
                    "예전에 건대부중 건물로 사용되었어요."
            28 -> "리빙디자인학과 학생들이 실습을 위해 사용해요. " +
                    "리빙디자인학과생들 말고는 잘 모르는 희귀의 건물이에요."
            29 -> "교원 창업 기업, 자회사, 캠퍼스타운 사업단을 운영하고 있어요. 과거에는 국제학사였어요."
            30 -> "기숙사에서는 취사가 안돼요. " +
                    "헬스장이 오픈했어요."
            31 -> "축제 때 보트를 띄우고 놀아요. " +
                    "수질이 좋지 않아요. " +
                    "수심이 매우 깊어 빠지면 죽을 수 있어요. " +
                    "안에 와우도라는 작은 무인도에 새가 많아 새똥을 조심해야해요. " +
                    "시험기간에 일감호에서 자라를 보면 시험 점수를 잘 받는다는 전설이 있어요."
            32 -> "썸일때 같이 건너면 사귀고, 커플일 때 같이 건너면 헤어진다는 전설이 있어요."
            33 -> "건대의 상징 황소의 동상이 있어요. " +
                    "새천년관 황소동상도 있어요."
            34 -> "좋은 말씀을 전하러오시는 분이 많아요. " +
                    "청심대 안에 자판기가 존재해요."
            35 -> "겨울에 눈사람 명소에요. " +
                    "황소동상과 더불어 졸업사진 명소에요."
            //36 -> "축제 공연을 할때 노천극장에서 주로 진행해요."
            37 -> "16년에 흙운동장에서 인공잔디로 바뀌었어요."
            38 -> "실내 체육관은 건국대학교 농구부가 주로 사용해요. 건국대학교 농구부는 2022년 대학 농구리그 준우승을 차지했어요."
            39 -> "건대입구에 비해 물가가 저렴하며 대학가 같아요. " +
                    "동아리 회식이 주로 이루어 져요. " +
                    "KT 학생증을 등록할 수 있어요."
            40 -> "이름은 중문이지만 따로 문은 없어요. " +
                    "중문 근처에 보드를 탈 수 있는 장소가 마련되어 있어요."
            41 -> "건너편에 건국대학교 동문회관이 있어요. 건대 동문은 할인을 해준다는 소문이 있으니 결혼할 사람들은 잘 찾아보는건 어떨까요?"
            42 -> "상허문을 시작으로 있는 비석들은 비석이 아니라 70개국 세계문자조형물이에요."
            43 -> "구의역, 구의역 자취촌으로 갈 수 있는 길이 있어요. " +
                    "구의역에는 광진구청이 있어 직장인 맛집이 많아요."
            44 -> "아는 사람들만 아는 꿀 통로에요."

            else -> "존재하지 않는 건물입니다."
        }
    }

    fun getImageDrawable() : Int {
        return when(this.id) {
            1 -> R.drawable.landmark_1
            2 -> R.drawable.landmark_2
            3 -> R.drawable.landmark_3
            4 -> R.drawable.landmark_4
            5 -> R.drawable.landmark_5
            6 -> R.drawable.landmark_6
            7 -> R.drawable.landmark_7
            8-> R.drawable.landmark_8
            9 -> R.drawable.landmark_9
            10 -> R.drawable.landmark_10
            11 -> R.drawable.landmark_11
            12 -> R.drawable.landmark_12
            13 -> R.drawable.landmark_13
            14 -> R.drawable.landmark_14
            15 -> R.drawable.landmark_15
            16 -> R.drawable.landmark_16
            17 -> R.drawable.landmark_17
            18 -> R.drawable.landmark_18
            19 -> R.drawable.landmark_19
            20 -> R.drawable.landmark_20
            21 -> R.drawable.landmark_21
            22 -> R.drawable.landmark_22
            23 -> R.drawable.landmark_23
            24 -> R.drawable.landmark_24
            25 -> R.drawable.landmark_25
            26 -> R.drawable.landmark_26
            27 -> R.drawable.landmark_27
            28 -> R.drawable.landmark_28
            29 -> R.drawable.landmark_29
            30 -> R.drawable.landmark_30
            31 -> R.drawable.landmark_31
            32 -> R.drawable.landmark_32
            33 -> R.drawable.landmark_33
            34 -> R.drawable.landmark_34
            35 -> R.drawable.landmark_35
//            36 -> R.drawable.landmark_36
            37 -> R.drawable.landmark_37
            38 -> R.drawable.landmark_38
            39 -> R.drawable.landmark_39
            40 -> R.drawable.landmark_40
            41 -> R.drawable.landmark_41
            42 -> R.drawable.landmark_42
            43 -> R.drawable.landmark_43
            44 -> R.drawable.landmark_44

            else -> R.color.lighter_gray
        }
    }

}
