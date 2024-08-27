package com.umc.playkuround.data

import com.umc.playkuround.R

data class Badge(
    var id : Int,
    var name : String,
    var description : String
) {

    companion object {
        const val ATTENDANCE_1 = "ATTENDANCE_1"
        const val ATTENDANCE_5 = "ATTENDANCE_5"
        const val ATTENDANCE_10 = "ATTENDANCE_10"
        const val ATTENDANCE_30 = "ATTENDANCE_30"
        const val ATTENDANCE_50 = "ATTENDANCE_50"
        const val ATTENDANCE_100 = "ATTENDANCE_100"

        const val ATTENDANCE_FOUNDATION_DAY = "ATTENDANCE_FOUNDATION_DAY"
        const val ATTENDANCE_ARBOR_DAY = "ATTENDANCE_ARBOR_DAY"
        const val ATTENDANCE_CHILDREN_DAY = "ATTENDANCE_CHILDREN_DAY"
        const val ATTENDANCE_WHITE_DAY = "ATTENDANCE_WHITE_DAY"
        const val ATTENDANCE_DUCK_DAY = "ATTENDANCE_DUCK_DAY"

        const val ATTENDANCE_CHUSEOK_DAY = "ATTENDANCE_CHUSEOK_DAY"
        const val ATTENDANCE_KOREAN_DAY = "ATTENDANCE_KOREAN_DAY"
        const val ATTENDANCE_DOKDO_DAY = "ATTENDANCE_DOKDO_DAY"
        const val ATTENDANCE_KIMCHI_DAY = "ATTENDANCE_KIMCHI_DAY"
        const val ATTENDANCE_CHRISTMAS_DAY = "ATTENDANCE_CHRISTMAS_DAY"

        const val COLLEGE_OF_ENGINEERING = "COLLEGE_OF_ENGINEERING"
        const val COLLEGE_OF_ART_AND_DESIGN = "COLLEGE_OF_ART_AND_DESIGN"
        const val COLLEGE_OF_BUSINESS_ADMINISTRATION = "COLLEGE_OF_BUSINESS_ADMINISTRATION"
        const val COLLEGE_OF_LIBERAL_ARTS = "COLLEGE_OF_LIBERAL_ARTS"
        const val COLLEGE_OF_SCIENCES = "COLLEGE_OF_SCIENCES"
        const val COLLEGE_OF_ARCHITECTURE = "COLLEGE_OF_ARCHITECTURE"
        const val COLLEGE_OF_SOCIAL_SCIENCES = "COLLEGE_OF_SOCIAL_SCIENCES"
        const val COLLEGE_OF_REAL_ESTATE = "COLLEGE_OF_REAL_ESTATE"
        const val COLLEGE_OF_INSTITUTE_TECHNOLOGY = "COLLEGE_OF_INSTITUTE_TECHNOLOGY"
        const val COLLEGE_OF_BIOLOGICAL_SCIENCES = "COLLEGE_OF_BIOLOGICAL_SCIENCES"
        const val COLLEGE_OF_VETERINARY_MEDICINE = "COLLEGE_OF_VETERINARY_MEDICINE"
        const val COLLEGE_OF_EDUCATION = "COLLEGE_OF_EDUCATION"

        const val COLLEGE_OF_SANG_HUH = "COLLEGE_OF_SANG_HUH"
        const val COLLEGE_OF_INTERNATIONAL = "COLLEGE_OF_INTERNATIONAL"

        const val COLLEGE_OF_BUSINESS_ADMINISTRATION_10 = "COLLEGE_OF_BUSINESS_ADMINISTRATION_10"
        const val COLLEGE_OF_BUSINESS_ADMINISTRATION_30 = "COLLEGE_OF_BUSINESS_ADMINISTRATION_30"
        const val COLLEGE_OF_BUSINESS_ADMINISTRATION_50 = "COLLEGE_OF_BUSINESS_ADMINISTRATION_50"
        const val COLLEGE_OF_BUSINESS_ADMINISTRATION_70 = "COLLEGE_OF_BUSINESS_ADMINISTRATION_70"
        const val COLLEGE_OF_BUSINESS_ADMINISTRATION_100_AND_FIRST_PLACE = "COLLEGE_OF_BUSINESS_ADMINISTRATION_100_AND_FIRST_PLACE"

        const val COLLEGE_OF_ART_AND_DESIGN_BEFORE_NOON = "COLLEGE_OF_ART_AND_DESIGN_BEFORE_NOON"
        const val COLLEGE_OF_ART_AND_DESIGN_AFTER_NOON = "COLLEGE_OF_ART_AND_DESIGN_AFTER_NOON"
        const val COLLEGE_OF_ART_AND_DESIGN_NIGHT = "COLLEGE_OF_ART_AND_DESIGN_NIGHT"

        const val COLLEGE_OF_ENGINEERING_A = "COLLEGE_OF_ENGINEERING_A"
        const val COLLEGE_OF_ENGINEERING_B = "COLLEGE_OF_ENGINEERING_B"
        const val COLLEGE_OF_ENGINEERING_C = "COLLEGE_OF_ENGINEERING_C"

        const val THE_DREAM_OF_DUCK = "THE_DREAM_OF_DUCK"

        const val MONTHLY_RANKING_1 = "MONTHLY_RANKING_1"
        const val MONTHLY_RANKING_2 = "MONTHLY_RANKING_2"
        const val MONTHLY_RANKING_3 = "MONTHLY_RANKING_3"

        const val BUSINESS_ARCHITECTURE_EVENT_BUSINESS = "BUSINESS_ARCHITECTURE_EVENT_BUSINESS"
        const val BUSINESS_ARCHITECTURE_EVENT_ARCHITECTURE = "BUSINESS_ARCHITECTURE_EVENT_ARCHITECTURE"
    }

    init {
        if(name.isEmpty()) {
            name = when(id) {
                0 -> ATTENDANCE_1
                1 -> ATTENDANCE_5
                2 -> ATTENDANCE_10
                3 -> ATTENDANCE_30
                4 -> ATTENDANCE_50
                5 -> ATTENDANCE_100
                6 -> ATTENDANCE_FOUNDATION_DAY
                7 -> ATTENDANCE_ARBOR_DAY
                8 -> ATTENDANCE_CHILDREN_DAY
                9 -> ATTENDANCE_WHITE_DAY
                10 -> ATTENDANCE_DUCK_DAY

                11 -> ATTENDANCE_CHUSEOK_DAY
                12 -> ATTENDANCE_KOREAN_DAY
                13 -> ATTENDANCE_DOKDO_DAY
                14 -> ATTENDANCE_KIMCHI_DAY
                15 -> ATTENDANCE_CHRISTMAS_DAY

                16 -> COLLEGE_OF_ENGINEERING
                17 -> COLLEGE_OF_ART_AND_DESIGN
                18 -> COLLEGE_OF_BUSINESS_ADMINISTRATION
                19 -> COLLEGE_OF_LIBERAL_ARTS
                20 -> COLLEGE_OF_SCIENCES
                21 -> COLLEGE_OF_ARCHITECTURE
                22 -> COLLEGE_OF_SOCIAL_SCIENCES
                23 -> COLLEGE_OF_REAL_ESTATE
                24 -> COLLEGE_OF_INSTITUTE_TECHNOLOGY
                25 -> COLLEGE_OF_BIOLOGICAL_SCIENCES
                26 -> COLLEGE_OF_VETERINARY_MEDICINE
                27 -> COLLEGE_OF_EDUCATION

                28 -> COLLEGE_OF_SANG_HUH
                29 -> COLLEGE_OF_INTERNATIONAL

                30 -> COLLEGE_OF_BUSINESS_ADMINISTRATION_10
                31 -> COLLEGE_OF_BUSINESS_ADMINISTRATION_30
                32 -> COLLEGE_OF_BUSINESS_ADMINISTRATION_50
                33 -> COLLEGE_OF_BUSINESS_ADMINISTRATION_70
                34 -> COLLEGE_OF_BUSINESS_ADMINISTRATION_100_AND_FIRST_PLACE

                35 -> COLLEGE_OF_ART_AND_DESIGN_BEFORE_NOON
                36 -> COLLEGE_OF_ART_AND_DESIGN_AFTER_NOON
                37 -> COLLEGE_OF_ART_AND_DESIGN_NIGHT

                38 -> COLLEGE_OF_ENGINEERING_A
                39 -> COLLEGE_OF_ENGINEERING_B
                40 -> COLLEGE_OF_ENGINEERING_C

                41 -> THE_DREAM_OF_DUCK

                42 -> MONTHLY_RANKING_1
                43 -> MONTHLY_RANKING_2
                44 -> MONTHLY_RANKING_3

                45 -> BUSINESS_ARCHITECTURE_EVENT_BUSINESS
                46 -> BUSINESS_ARCHITECTURE_EVENT_ARCHITECTURE

                else -> ""
            }
        }
        description = when(name) {
            ATTENDANCE_1 -> "첫 출석을 축하드려요!"
            ATTENDANCE_5 -> "5일 출석하셨어요!"
            ATTENDANCE_10 -> "10일 출석하셨어요!"
            ATTENDANCE_30 -> "30일 출석하셨어요!"
            ATTENDANCE_50 -> "50일 출석하셨어요!"
            ATTENDANCE_100 -> "100일 출석하셨어요!"
            ATTENDANCE_FOUNDATION_DAY -> "건국대학교의 개교기념일 5월 15일 쿠라운드에서 축하해요!"
            ATTENDANCE_ARBOR_DAY -> "오늘은 식목일! 오늘만이라도 나무들에게 칭찬 한마디~"
            ATTENDANCE_CHILDREN_DAY -> "마음이 어린이면 어린이 아닌가요? 우리의 날을 축하하며!"
            ATTENDANCE_WHITE_DAY -> "화이트데이를 기념해 쿠라운드에서 달콤한 사탕 뱃지를 드립니다!"
            ATTENDANCE_DUCK_DAY -> "덕쿠의날을 기록하며 덕쿠야 축하해!"

            ATTENDANCE_CHUSEOK_DAY -> "풍요로운 추석 보내세요! 덕쿠 절 드립니다!"
            ATTENDANCE_KOREAN_DAY -> "훈민정음 창제를 기념하고 우리 한글의 우수성을 알리며!"
            ATTENDANCE_DOKDO_DAY -> "뱃길따라 이백리~ 독도는 우리땅!"
            ATTENDANCE_KIMCHI_DAY -> "도둑 덕쿠도 못참는 한국의 전통음식 김치! 김치의 날을 기념해요!"
            ATTENDANCE_CHRISTMAS_DAY -> "Merry Christmas! 모두 행복한 크리스마스 보내세요"

            COLLEGE_OF_ENGINEERING -> "공과대학을 탐험했어요"
            COLLEGE_OF_ART_AND_DESIGN -> "예술디자인대학을 탐험했어요"
            COLLEGE_OF_BUSINESS_ADMINISTRATION -> "경영대학을 탐험했어요"
            COLLEGE_OF_LIBERAL_ARTS -> "문과대학을 탐험했어요"
            COLLEGE_OF_SCIENCES -> "이과대학을 탐험했어요"
            COLLEGE_OF_ARCHITECTURE -> "건축대학을 탐험했어요"
            COLLEGE_OF_SOCIAL_SCIENCES -> "사회과학대학을 탐험했어요"
            COLLEGE_OF_REAL_ESTATE -> "부동산과학원을 탐험했어요"
            COLLEGE_OF_INSTITUTE_TECHNOLOGY -> "융합과학기술원을 탐험했어요"
            COLLEGE_OF_BIOLOGICAL_SCIENCES -> "생명과학대학을 탐험했어요"
            COLLEGE_OF_VETERINARY_MEDICINE -> "수의과대학을 탐험했어요"
            COLLEGE_OF_EDUCATION -> "사범대학을 탐험했어요"

            COLLEGE_OF_SANG_HUH -> "상허교양대학을 탐험했어요"
            COLLEGE_OF_INTERNATIONAL -> "국제대학을 탐험했어요"

            COLLEGE_OF_BUSINESS_ADMINISTRATION_10 -> "네..! 넵의 연속 나는야 새싹 인턴"
            COLLEGE_OF_BUSINESS_ADMINISTRATION_30 -> "이걸 제가요? 어엿한 대리 승급!"
            COLLEGE_OF_BUSINESS_ADMINISTRATION_50 -> "라떼는 말이야… 실무와 관리의 공존 과장"
            COLLEGE_OF_BUSINESS_ADMINISTRATION_70 -> "어째서 내가 리더..?\n경쟁을 이끌어! 부장"
            COLLEGE_OF_BUSINESS_ADMINISTRATION_100_AND_FIRST_PLACE -> "john 우리의 수익을 develop할 수 있는 방안 없을까? 비전과 열정 탑 CEO"

            COLLEGE_OF_ART_AND_DESIGN_BEFORE_NOON -> "예디대생들 피엔 카페인이 흘러.. "
            COLLEGE_OF_ART_AND_DESIGN_AFTER_NOON -> "영차영차 오늘도 덕쿠는 작업중"
            COLLEGE_OF_ART_AND_DESIGN_NIGHT -> "너 야작해..?\n예디대생들은 야작의 노예"

            COLLEGE_OF_ENGINEERING_A -> "당신을 공대 A관의 공돌이로 임명합니다!"
            COLLEGE_OF_ENGINEERING_B -> "당신을 공대 B관의 공돌이로 임명합니다!"
            COLLEGE_OF_ENGINEERING_C -> "당신을 공대 C관의 공돌이로 임명합니다!"

            THE_DREAM_OF_DUCK -> "컷신을 모두 보셨군요!\n쿠라운드를 즐길 준비 완료!"

            MONTHLY_RANKING_1 -> "월간 랭킹 1위 기록을 축하드립니다!"
            MONTHLY_RANKING_2 -> "월간 랭킹 2위 기록을 축하드립니다!"
            MONTHLY_RANKING_3 -> "월간 랭킹 3위 기록을 축하드립니다!"

            BUSINESS_ARCHITECTURE_EVENT_BUSINESS -> "경영대학의 힘을 보여줘! 와우도를 차지하자!"
            BUSINESS_ARCHITECTURE_EVENT_ARCHITECTURE -> "건축대학의 힘을 보여줘! 와우도를 차지하자!"

            else -> "알 수 없는 배지입니다."
        }
        id = when(name) {
            ATTENDANCE_1 -> 0
            ATTENDANCE_5 -> 1
            ATTENDANCE_10 -> 2
            ATTENDANCE_30 -> 3
            ATTENDANCE_50 -> 4
            ATTENDANCE_100 -> 5
            ATTENDANCE_FOUNDATION_DAY -> 6
            ATTENDANCE_ARBOR_DAY -> 7
            ATTENDANCE_CHILDREN_DAY -> 8
            ATTENDANCE_WHITE_DAY -> 9
            ATTENDANCE_DUCK_DAY -> 10

            ATTENDANCE_CHUSEOK_DAY -> 11
            ATTENDANCE_KOREAN_DAY -> 12
            ATTENDANCE_DOKDO_DAY -> 13
            ATTENDANCE_KIMCHI_DAY -> 14
            ATTENDANCE_CHRISTMAS_DAY -> 15

            COLLEGE_OF_ENGINEERING -> 16
            COLLEGE_OF_ART_AND_DESIGN -> 17
            COLLEGE_OF_BUSINESS_ADMINISTRATION -> 18
            COLLEGE_OF_LIBERAL_ARTS -> 19
            COLLEGE_OF_SCIENCES -> 20
            COLLEGE_OF_ARCHITECTURE -> 21
            COLLEGE_OF_SOCIAL_SCIENCES -> 22
            COLLEGE_OF_REAL_ESTATE -> 23
            COLLEGE_OF_INSTITUTE_TECHNOLOGY -> 24
            COLLEGE_OF_BIOLOGICAL_SCIENCES -> 25
            COLLEGE_OF_VETERINARY_MEDICINE -> 26
            COLLEGE_OF_EDUCATION -> 27

            COLLEGE_OF_SANG_HUH -> 28
            COLLEGE_OF_INTERNATIONAL -> 29

            COLLEGE_OF_BUSINESS_ADMINISTRATION_10 -> 30
            COLLEGE_OF_BUSINESS_ADMINISTRATION_30 -> 31
            COLLEGE_OF_BUSINESS_ADMINISTRATION_50 -> 32
            COLLEGE_OF_BUSINESS_ADMINISTRATION_70 -> 33
            COLLEGE_OF_BUSINESS_ADMINISTRATION_100_AND_FIRST_PLACE -> 34

            COLLEGE_OF_ART_AND_DESIGN_BEFORE_NOON -> 35
            COLLEGE_OF_ART_AND_DESIGN_AFTER_NOON -> 36
            COLLEGE_OF_ART_AND_DESIGN_NIGHT -> 37

            COLLEGE_OF_ENGINEERING_A -> 38
            COLLEGE_OF_ENGINEERING_B -> 39
            COLLEGE_OF_ENGINEERING_C -> 40

            THE_DREAM_OF_DUCK -> 41

            MONTHLY_RANKING_1 -> 42
            MONTHLY_RANKING_2 -> 43
            MONTHLY_RANKING_3 -> 44

            BUSINESS_ARCHITECTURE_EVENT_BUSINESS -> 45
            BUSINESS_ARCHITECTURE_EVENT_ARCHITECTURE -> 46

            else -> -1
        }
    }

    fun getImageDrawable() : Int {
        return when(name) {
            ATTENDANCE_1 -> R.drawable.badge_attendance_first
            ATTENDANCE_5 -> R.drawable.badge_attendance_5
            ATTENDANCE_10 -> R.drawable.badge_attendance_10
            ATTENDANCE_30 -> R.drawable.badge_attendance_30
            ATTENDANCE_50 -> R.drawable.badge_attendance_50
            ATTENDANCE_100 -> R.drawable.badge_attendance_100
            ATTENDANCE_FOUNDATION_DAY -> R.drawable.badge_attendance_foundation_day
            ATTENDANCE_ARBOR_DAY -> R.drawable.badge_attendance_arbor_day
            ATTENDANCE_CHILDREN_DAY -> R.drawable.badge_attendance_children_day
            ATTENDANCE_WHITE_DAY -> R.drawable.badge_attendance_white_day
            ATTENDANCE_DUCK_DAY -> R.drawable.badge_attendance_duck_day

            ATTENDANCE_CHUSEOK_DAY -> R.drawable.badge_attendance_chuseok
            ATTENDANCE_KOREAN_DAY -> R.drawable.badge_attendance_korean
            ATTENDANCE_DOKDO_DAY -> R.drawable.badge_attendance_dokdo
            ATTENDANCE_KIMCHI_DAY -> R.drawable.badge_attendance_kimchi
            ATTENDANCE_CHRISTMAS_DAY -> R.drawable.badge_attendance_christmas

            COLLEGE_OF_ENGINEERING -> R.drawable.badge_engineer
            COLLEGE_OF_ART_AND_DESIGN -> R.drawable.badge_artist
            COLLEGE_OF_BUSINESS_ADMINISTRATION -> R.drawable.badge_ceo
            COLLEGE_OF_LIBERAL_ARTS -> R.drawable.badge_college_of_liberal_arts
            COLLEGE_OF_SCIENCES -> R.drawable.badge_college_of_sciences
            COLLEGE_OF_ARCHITECTURE -> R.drawable.badge_college_of_architecture
            COLLEGE_OF_SOCIAL_SCIENCES -> R.drawable.badge_college_of_social_sciences
            COLLEGE_OF_REAL_ESTATE -> R.drawable.badge_college_of_real_estate
            COLLEGE_OF_INSTITUTE_TECHNOLOGY -> R.drawable.badge_college_of_institute_technology
            COLLEGE_OF_BIOLOGICAL_SCIENCES -> R.drawable.badge_college_of_biological_sciences
            COLLEGE_OF_VETERINARY_MEDICINE -> R.drawable.badge_college_of_veterinary_medicine
            COLLEGE_OF_EDUCATION -> R.drawable.badge_college_of_education

            COLLEGE_OF_SANG_HUH -> R.drawable.badge_college_of_sanghuh
            COLLEGE_OF_INTERNATIONAL -> R.drawable.badge_college_of_international

            COLLEGE_OF_BUSINESS_ADMINISTRATION_10 -> R.drawable.badge_college_of_business_administration_10
            COLLEGE_OF_BUSINESS_ADMINISTRATION_30 -> R.drawable.badge_college_of_business_administration_30
            COLLEGE_OF_BUSINESS_ADMINISTRATION_50 -> R.drawable.badge_college_of_business_administration_50
            COLLEGE_OF_BUSINESS_ADMINISTRATION_70 -> R.drawable.badge_college_of_business_administration_70
            COLLEGE_OF_BUSINESS_ADMINISTRATION_100_AND_FIRST_PLACE -> R.drawable.badge_college_of_business_administration_100_and_first_place

            COLLEGE_OF_ART_AND_DESIGN_BEFORE_NOON -> R.drawable.badge_college_of_art_and_design_before_noon
            COLLEGE_OF_ART_AND_DESIGN_AFTER_NOON -> R.drawable.badge_college_of_art_and_design_after_noon
            COLLEGE_OF_ART_AND_DESIGN_NIGHT -> R.drawable.badge_college_of_art_and_design_night

            COLLEGE_OF_ENGINEERING_A -> R.drawable.badge_college_of_engineering_a
            COLLEGE_OF_ENGINEERING_B -> R.drawable.badge_college_of_engineering_b
            COLLEGE_OF_ENGINEERING_C -> R.drawable.badge_college_of_engineering_c

            THE_DREAM_OF_DUCK -> R.drawable.badge_the_dream_of_duck

            MONTHLY_RANKING_1 -> R.drawable.badge_monthly_ranking_1
            MONTHLY_RANKING_2 -> R.drawable.badge_monthly_ranking_3
            MONTHLY_RANKING_3 -> R.drawable.badge_monthly_ranking_2

            BUSINESS_ARCHITECTURE_EVENT_BUSINESS -> R.drawable.badge_event_business
            BUSINESS_ARCHITECTURE_EVENT_ARCHITECTURE -> R.drawable.badge_event_architecture

            else -> R.drawable.badge_locked
        }
    }

    fun getTitle() : String {
        return when(name) {
            ATTENDANCE_1 -> "첫 출석"
            ATTENDANCE_5 -> "5일 출석"
            ATTENDANCE_10 -> "10일 출석"
            ATTENDANCE_30 -> "30일 출석"
            ATTENDANCE_50 -> "50일 출석"
            ATTENDANCE_100 -> "100일 출석"
            ATTENDANCE_FOUNDATION_DAY -> "개교기념일"
            ATTENDANCE_ARBOR_DAY -> "식목일"
            ATTENDANCE_CHILDREN_DAY -> "어린이날"
            ATTENDANCE_WHITE_DAY -> "화이트데이"
            ATTENDANCE_DUCK_DAY -> "오리의날"

            ATTENDANCE_CHUSEOK_DAY -> "추석"
            ATTENDANCE_KOREAN_DAY -> "한글날"
            ATTENDANCE_DOKDO_DAY -> "독도 덕쿠"
            ATTENDANCE_KIMCHI_DAY -> "김치도둑 덕쿠"
            ATTENDANCE_CHRISTMAS_DAY -> "성탄절"

            COLLEGE_OF_ENGINEERING -> "공과대"
            COLLEGE_OF_ART_AND_DESIGN -> "예디대"
            COLLEGE_OF_BUSINESS_ADMINISTRATION -> "경영대"
            COLLEGE_OF_LIBERAL_ARTS -> "문과대"
            COLLEGE_OF_SCIENCES -> "이과대"
            COLLEGE_OF_ARCHITECTURE -> "건축대"
            COLLEGE_OF_SOCIAL_SCIENCES -> "사과대"
            COLLEGE_OF_REAL_ESTATE -> "부동산"
            COLLEGE_OF_INSTITUTE_TECHNOLOGY -> "융과기"
            COLLEGE_OF_BIOLOGICAL_SCIENCES -> "생과대"
            COLLEGE_OF_VETERINARY_MEDICINE -> "수의대"
            COLLEGE_OF_EDUCATION -> "사범대"

            COLLEGE_OF_SANG_HUH -> "상허교양대"
            COLLEGE_OF_INTERNATIONAL -> "국제대"

            COLLEGE_OF_BUSINESS_ADMINISTRATION_10 -> "인턴"
            COLLEGE_OF_BUSINESS_ADMINISTRATION_30 -> "대리"
            COLLEGE_OF_BUSINESS_ADMINISTRATION_50 -> "과장"
            COLLEGE_OF_BUSINESS_ADMINISTRATION_70 -> "부장"
            COLLEGE_OF_BUSINESS_ADMINISTRATION_100_AND_FIRST_PLACE -> "CEO"

            COLLEGE_OF_ART_AND_DESIGN_BEFORE_NOON -> "카페인 노예 덕쿠"
            COLLEGE_OF_ART_AND_DESIGN_AFTER_NOON -> "덕쿠는 작업중"
            COLLEGE_OF_ART_AND_DESIGN_NIGHT -> "야작하는 덕쿠"

            COLLEGE_OF_ENGINEERING_A -> "공대A"
            COLLEGE_OF_ENGINEERING_B -> "공대B"
            COLLEGE_OF_ENGINEERING_C -> "공대C"

            THE_DREAM_OF_DUCK -> "오리의꿈"

            MONTHLY_RANKING_1 -> "금메달"
            MONTHLY_RANKING_2 -> "은메달"
            MONTHLY_RANKING_3 -> "동메달"

            BUSINESS_ARCHITECTURE_EVENT_BUSINESS -> "경영오리 비쿠"
            BUSINESS_ARCHITECTURE_EVENT_ARCHITECTURE -> "건축오리 어쿠"

            else -> "없는 배지"
        }
    }

    fun getCondition() : String {
        return when(name) {
            ATTENDANCE_1 -> "플레이쿠라운드 출석 1회 시 잠금 해제됩니다."
            ATTENDANCE_5 -> "플레이쿠라운드 출석 5회 시 잠금 해제됩니다."
            ATTENDANCE_10 -> "플레이쿠라운드 출석 10회 시 잠금 해제됩니다."
            ATTENDANCE_30 -> "플레이쿠라운드 출석 30회 시 잠금 해제됩니다."
            ATTENDANCE_50 -> "플레이쿠라운드 출석 50회 시 잠금 해제됩니다."
            ATTENDANCE_100 -> "플레이쿠라운드 출석 100회 시 잠금 해제됩니다."
            ATTENDANCE_FOUNDATION_DAY -> "개교기념일(5/15) 출석 시 잠금 해제됩니다."
            ATTENDANCE_ARBOR_DAY -> "식목일(4/5) 출석 시 잠금 해제됩니다."
            ATTENDANCE_CHILDREN_DAY -> "어린이날(5/5) 출석 시 잠금 해제됩니다."
            ATTENDANCE_WHITE_DAY -> "화이트데이(3/14) 출석 시 잠금 해제됩니다."
            ATTENDANCE_DUCK_DAY -> "오리의날(5/2) 출석 시 잠금 해제됩니다."

            ATTENDANCE_CHUSEOK_DAY -> "추석(9/16) 출석 시 잠금 해제됩니다."
            ATTENDANCE_KOREAN_DAY -> "한글날(10/19) 출석 시 잠금 해제됩니다."
            ATTENDANCE_DOKDO_DAY -> "독도의 날(10/25) 출석 시 잠금 해제됩니다."
            ATTENDANCE_KIMCHI_DAY -> "김치의 날(11/22) 출석 시 잠금 해제됩니다."
            ATTENDANCE_CHRISTMAS_DAY -> "성탄절(12/25) 출석 시 잠금 해제됩니다."

            COLLEGE_OF_ENGINEERING -> "공학관 A, B, C, 신공학관 탐험 시 잠금 해제됩니다."
            COLLEGE_OF_ART_AND_DESIGN -> "예술디자인관, 공예관 탐험 시 잠금 해제됩니다."
            COLLEGE_OF_BUSINESS_ADMINISTRATION -> "경영관 탐험 시 잠금 해제됩니다."
            COLLEGE_OF_LIBERAL_ARTS -> "인문학관 탐험 시 잠금 해제됩니다."
            COLLEGE_OF_SCIENCES -> "과학관 탐험 시 잠금 해제됩니다."
            COLLEGE_OF_ARCHITECTURE -> "건축관 탐험 시 잠금 해제됩니다."
            COLLEGE_OF_SOCIAL_SCIENCES -> "상허연구관 탐험 시 잠금 해제됩니다."
            COLLEGE_OF_REAL_ESTATE -> "부동산학관 탐험 시 잠금 해제됩니다."
            COLLEGE_OF_INSTITUTE_TECHNOLOGY -> "공학관 A, B, C, 생명공학관 탐험 시 잠금 해제됩니다."
            COLLEGE_OF_BIOLOGICAL_SCIENCES -> "동물생명과학관 탐험 시 잠금 해제됩니다."
            COLLEGE_OF_VETERINARY_MEDICINE -> "수의학관 탐험 시 잠금 해제됩니다."
            COLLEGE_OF_EDUCATION -> "교육과학관 탐험 시 잠금 해제됩니다."

            COLLEGE_OF_SANG_HUH -> "산학협동관 탐험 시 잠금 해제됩니다."
            COLLEGE_OF_INTERNATIONAL -> "법학관 탐험 시 잠금 해제됩니다."

            COLLEGE_OF_BUSINESS_ADMINISTRATION_10 -> "경영대 건물에서 10회 게임 실행 시 잠금 해제됩니다."
            COLLEGE_OF_BUSINESS_ADMINISTRATION_30 -> "경영대 건물에서 30회 게임 실행 시 잠금 해제됩니다."
            COLLEGE_OF_BUSINESS_ADMINISTRATION_50 -> "경영대 건물에서 50회 게임 실행 시 잠금 해제됩니다."
            COLLEGE_OF_BUSINESS_ADMINISTRATION_70 -> "경영대 건물에서 70회 게임 실행 시 잠금 해제됩니다."
            COLLEGE_OF_BUSINESS_ADMINISTRATION_100_AND_FIRST_PLACE -> "경영대 건물에서 100회 게임 실행 시 잠금 해제됩니다."

            COLLEGE_OF_ART_AND_DESIGN_BEFORE_NOON -> "9:00 ~ 11:59 사이 예디대 건물에서 게임 실행 시 잠금 해제됩니다."
            COLLEGE_OF_ART_AND_DESIGN_AFTER_NOON -> "12:00 ~ 18:00 사이 예디대 건물에서 게임 실행 시 잠금 해제됩니다."
            COLLEGE_OF_ART_AND_DESIGN_NIGHT -> "23:00 ~ 4:00 사이 예디대 건물에서 게임 실행 시 잠금 해제됩니다."

            COLLEGE_OF_ENGINEERING_A -> "공대 A관에서 게임 10회 이상 게임 실행 시 잠금 해제됩니다."
            COLLEGE_OF_ENGINEERING_B -> "공대 B관에서 게임 10회 이상 게임 실행 시 잠금 해제됩니다."
            COLLEGE_OF_ENGINEERING_C -> "공대 C관에서 게임 10회 이상 게임 실행 시 잠금 해제됩니다."

            THE_DREAM_OF_DUCK -> "6개의 스토리 컷씬 모두 보면 잠금 해제됩니다."

            MONTHLY_RANKING_1 -> "월간 전체 랭킹 1등 시 잠금 해제됩니다."
            MONTHLY_RANKING_2 -> "월간 전체 랭킹 2등 시 잠금 해제됩니다."
            MONTHLY_RANKING_3 -> "월간 전체 랭킹 3등 시 잠금 해제됩니다."

            BUSINESS_ARCHITECTURE_EVENT_BUSINESS -> "경영X건축 이벤트 와우도 쟁탈전에 참여하는 경영대학 학생에게 지급됩니다."
            BUSINESS_ARCHITECTURE_EVENT_ARCHITECTURE -> "경영X건축 이벤트 와우도 쟁탈전에 참여하는 건축대학 학생에게 지급됩니다."

            else -> "없는 배지"
        }
    }

}
