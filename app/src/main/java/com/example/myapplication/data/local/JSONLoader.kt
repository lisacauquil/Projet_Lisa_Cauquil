package com.example.myapplication.data.local

import android.content.Context
import com.example.myapplication.data.model.Magasin
import org.json.JSONObject

class JSONLoader(private val context: Context) {

    fun loadMagasins(): List<Magasin> {
        val magasins = mutableListOf<Magasin>()
        val jsonString = context.assets.open("magasins.json").bufferedReader().use { it.readText() }
        val jsonObject = JSONObject(jsonString)
        val featuresArray = jsonObject.getJSONArray("features")

        for (i in 0 until featuresArray.length()) {
            val feature = featuresArray.getJSONObject(i)
            val properties = feature.getJSONObject("properties")
            val geometry = feature.getJSONObject("geometry")
            val coordinates = geometry.getJSONArray("coordinates")

            val longitude = coordinates.getDouble(0)
            val latitude = coordinates.getDouble(1)

            val nom = properties.optString("name", "Magasin inconnu")
            val adresse = when {
                properties.has("addr:street") -> properties.getString("addr:street")
                properties.has("contact:street") -> properties.getString("contact:street")
                else -> "Adresse inconnue"
            }

            val typeShop = properties.optString("shop", "")
            val passion = when (typeShop) {
                "books" -> "Livres"
                "sports" -> "Sport"
                "jewelry" -> "Montres"
                else -> "Autre"
            }

            val partenaire = (0..1).random() == 1

            val stock = if (partenaire) {
                when (passion) {
                    "Livres" -> listOf("Harry Potter", "Le Petit Prince", "L'Alchimiste", "1984")
                    "Montres" -> listOf("Rolex Submariner", "Omega Speedmaster", "TAG Heuer Carrera")
                    "Sport" -> listOf("Ballon de foot Adidas", "Raquette Babolat", "Chaussures Nike Running")
                    else -> listOf("Produit générique")
                }
            } else {
                emptyList()
            }

            magasins.add(
                Magasin(
                    nom = nom,
                    adresse = adresse,
                    latitude = latitude,
                    longitude = longitude,
                    passion = passion,
                    partenaire = partenaire,
                    stock = stock
                )
            )
        }

        return magasins
    }
}