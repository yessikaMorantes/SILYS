package com.silys.utils

import org.json.JSONArray
import org.json.JSONObject

class Utils {
    public fun convertJSONArrayToListJSON(jsonArray: JSONArray): List<JSONObject>{
        val mutableList = mutableListOf<JSONObject>()

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            mutableList.add(obj)
        }
        return mutableList;
    }
    public fun convertJSONArrayToList(jsonArray: JSONArray): List<String>{
        val mutableList = mutableListOf<String>()

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.optString(i)
            mutableList.add(obj)
        }
        return mutableList;
    }
}