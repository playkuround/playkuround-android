package com.umc.playkuround.data

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName(value = "email") var email : String,
    @SerializedName(value = "nickname") var nickname : String,
    @SerializedName(value = "major") var major : String
)