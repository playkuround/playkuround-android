package com.umc.playkuround.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.umc.playkuround.R

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
    private var icons = HashMap<String, Int>()

    init {
        icons["편의점"] = R.drawable.tag_icon_store
        icons["카페"] = R.drawable.tag_icon_cafe
        icons["복사기"] = R.drawable.tag_icon_printer
        icons["따릉이"] = R.drawable.tag_icon_bicycle
        icons["케이큐브"] = R.drawable.tag_icon_kcube
        icons["복사실"] = R.drawable.tag_icon_printer
        icons["증명발급서비스"] = R.drawable.tag_icon_document
        icons["도서반납기"] = R.drawable.tag_icon_book
        icons["증명서발급기"] = R.drawable.tag_icon_document

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

    fun getAmenity(id : Int) : List<Pair<String, Int>> {
        val amenityList = ArrayList<Pair<String, Int>>()
        landmarks[id - 1].amenity.forEach {
            val resId : Int? = icons[it]
            if(resId != null) amenityList.add(Pair(it, resId))
        }
        return amenityList
    }

}