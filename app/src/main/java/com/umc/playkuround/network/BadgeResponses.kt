package com.umc.playkuround.network

import com.google.gson.annotations.SerializedName

data class GetBadgeResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("response") val response: BadgeInfo
)

data class BadgeInfo(
    @SerializedName("newBadges") val newBadges: List<Badge>
)

data class Badge(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String
)

data class UserBadge(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("createdAt") val createdAt: String
)

data class UserBadgeResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("response") val badges: List<UserBadge>
)

data class ManualBadgeData(
    @SerializedName("userEmail") val userEmail: String,
    @SerializedName("badge") val badge: String,
    @SerializedName("registerMessage") val registerMessage: Boolean
)

data class BadgeResultResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("response") val response: Boolean
)