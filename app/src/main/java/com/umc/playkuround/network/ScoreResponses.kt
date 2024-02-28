package com.umc.playkuround.network

import com.google.gson.annotations.SerializedName

data class Top100Response(
    @SerializedName(value = "isSuccess") var isSuccess : Boolean,
    @SerializedName(value = "response") var responseData : Top100ResponseData
)

data class Top100ResponseData(
    @SerializedName("myRank") val myRank: MyRank,
    @SerializedName("rank") val rank: List<RankItem>
)

data class MyRank(
    @SerializedName("ranking") val ranking: Int,
    @SerializedName("score") val score: Int
)

data class RankItem(
    @SerializedName("nickname") val nickname: String?,
    @SerializedName("score") val score: Int
)
