package com.example.myapplication.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.myapplication.data.model.Magasin
import com.example.myapplication.data.local.loadJSONFromAsset
import kotlin.text.get

class MagasinRepository(private val context: Context) {

    fun loadMagasins(): List<Magasin> {
        val jsonString = loadJSONFromAsset(context, "magasins.json")
        return jsonString?.let {
            val mapType = object : TypeToken<Map<String, List<Magasin>>>() {}.type
            val magasinsMap = Gson().fromJson<Map<String, List<Magasin>>>(it, mapType)
            magasinsMap["magasins"] ?: emptyList()
        } ?: emptyList()
    }
}
