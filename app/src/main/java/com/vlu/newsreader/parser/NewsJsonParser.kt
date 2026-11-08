package com.vlu.newsreader.parser

import com.google.gson.Gson
import com.vlu.newsreader.model.NewsItem
import org.json.JSONArray

object NewsJsonParser {

    fun parseManually(jsonString: String): List<NewsItem> {
        val jsonArray = JSONArray(jsonString)
        val newsItems = ArrayList<NewsItem>(jsonArray.length())

        for (index in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(index)
            newsItems.add(
                NewsItem(
                    id = jsonObject.getInt("id"),
                    userId = jsonObject.getInt("userId"),
                    title = jsonObject.getString("title"),
                    body = jsonObject.getString("body")
                )
            )
        }

        return newsItems
    }

    fun parseWithGson(jsonString: String): List<NewsItem> {
        return Gson()
            .fromJson(jsonString, Array<NewsItem>::class.java)
            .toList()
    }
}
