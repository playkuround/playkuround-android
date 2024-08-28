package com.umc.playkuround.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class LandMarkJson(
    val id: Int,
    val description: List<String>,
    val information: List<Information>,
    val amenity: List<String>
)

data class Information(
    val title: String,
    val content: String
)

class LandmarkUtil(context : Context) {

    private var landmarks : List<LandMarkJson>

    init {
        val jsonString = readJsonFromAssets(context, "landmark.json")
        landmarks = parseBuildings(jsonString)
    }

    private fun readJsonFromAssets(context: Context, fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }

    private fun parseBuildings(jsonString: String): List<LandMarkJson> {
        val gson = Gson()
        val buildingType = object : TypeToken<List<LandMarkJson>>() {}.type
        return gson.fromJson(jsonString, buildingType)
    }

    fun getDescription(id : Int) : String {
        var description =  ""
        landmarks[id - 1].description.forEach {
            description += it
            description += " "
        }
        return description
    }

    fun getInformation(id : Int) : List<Information> {
        return landmarks[id - 1].information
    }

    fun getAmenity(id : Int) : List<String> {
        return landmarks[id - 1].amenity
    }

}