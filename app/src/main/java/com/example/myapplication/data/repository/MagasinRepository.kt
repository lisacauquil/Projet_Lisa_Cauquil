package com.example.myapplication.data.repository

import android.content.Context
import com.example.myapplication.data.local.JSONLoader
import com.example.myapplication.data.model.Magasin

class MagasinRepository(private val context: Context) {

    fun loadMagasins(): List<Magasin> {
        return JSONLoader(context).loadMagasins()
    }
}