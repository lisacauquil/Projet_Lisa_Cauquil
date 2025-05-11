package com.example.myapplication.data.repository

import android.content.Context
import com.example.myapplication.data.local.JSONLoader
import com.example.myapplication.data.model.Magasin

/**
 * Repository pour gérer l'accès aux données des magasins.
 * Utilise un fichier JSON local pour charger les informations des magasins.
 *
 * @param context Le contexte de l'application ou de l'activité.
 */
class MagasinRepository(private val context: Context) {

    // Instance du JSONLoader pour accéder aux données JSON
    private val jsonLoader = JSONLoader(context)

    /**
     * Charge la liste des magasins depuis le fichier `magasins.json`.
     *
     * @return Une liste de `Magasin`.
     *         Retourne une liste vide si le fichier JSON est introuvable ou invalide.
     */
    fun getMagasins(): List<Magasin> {

        // Charge le contenu du fichier `magasins.json` sous forme de chaîne JSON
        val jsonString = jsonLoader.loadJSONFromAsset("magasins.json") ?: return emptyList()

        // Convertit la chaîne JSON en une liste de `Magasin`
        return jsonLoader.parseMagasinsJSON(jsonString)
    }
}