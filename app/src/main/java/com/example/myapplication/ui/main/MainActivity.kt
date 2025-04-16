package com.example.myapplication.ui.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.model.Magasin
import com.example.myapplication.data.repository.MagasinRepository

class MainActivity : AppCompatActivity() {

    private lateinit var listeMagasins: List<Magasin>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = MagasinRepository(applicationContext)
        listeMagasins = repository.loadMagasins()

        Log.d("Magasins", "Nombre de magasins chargés : ${listeMagasins.size}")
        listeMagasins.take(5).forEach {
            Log.d("Magasin", "${it.nom} - ${it.passion} - ${if (it.partenaire) "Partenaire" else "Non partenaire"}")
        }
    }
}
