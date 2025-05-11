package com.example.myapplication.data.local

import android.content.Context
import java.io.IOException
import java.nio.charset.Charset
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import com.example.myapplication.data.model.Magasin

/**
 * Classe responsable du chargement et du parsing des fichiers JSON depuis les assets.
 *
 * @param context Le contexte de l'application, utilisé pour accéder aux assets.
 */
class JSONLoader(private val context: Context) {

    /**
     * Charge le contenu d'un fichier JSON depuis le dossier `assets`.
     *
     * @param fileName Le nom du fichier JSON (ex : "magasins.json").
     * @return Le contenu du fichier JSON sous forme de `String`, ou `null` en cas d'erreur.
     */
    fun loadJSONFromAsset(fileName: String): String? {
        return try {
            // Ouverture du fichier dans les assets
            val inputStream = context.assets.open(fileName)

            // Lecture de la taille du fichier
            val size = inputStream.available()

            // Création d'un tableau de bytes pour stocker le contenu
            val buffer = ByteArray(size)

            // Lecture du fichier dans le buffer
            inputStream.read(buffer)
            inputStream.close()

            // Conversion des bytes en chaîne UTF-8
            String(buffer, Charset.forName("UTF-8"))

        } catch (ex: IOException) {
            ex.printStackTrace()  // Affiche l'erreur en console
            null  // Retourne `null` en cas d'exception
        }
    }

    /**
     * Parse une chaîne JSON pour créer une liste de `Magasin`.
     *
     * @param jsonString La chaîne JSON à parser.
     * @return Une liste de `Magasin`.
     */
    fun parseMagasinsJSON(jsonString: String): List<Magasin> {

        // Liste des magasins à retourner
        val magasins = mutableListOf<Magasin>()

        try {
            // Création de l'objet JSON principal
            val jsonObject = JSONObject(jsonString)

            // Récupération du tableau `magasins`
            val magasinsArray: JSONArray = jsonObject.getJSONArray("magasins")

            // Parcours de chaque objet `Magasin`
            for (i in 0 until magasinsArray.length()) {
                val magasinObject = magasinsArray.getJSONObject(i)

                // Création d'un objet `Magasin`
                val magasin = Magasin(
                    nom = magasinObject.getString("nom"),
                    adresse = magasinObject.getString("adresse"),
                    latitude = magasinObject.getDouble("latitude"),
                    longitude = magasinObject.getDouble("longitude"),
                    passion = magasinObject.getString("passion"),
                    partenaire = magasinObject.getBoolean("partenaire"),

                    // Si le magasin est partenaire, on récupère le stock
                    stock = if (magasinObject.has("stock")) {
                        val stockArray = magasinObject.getJSONArray("stock")
                        // Conversion du tableau JSON en `List<String>`
                        List(stockArray.length()) { stockArray.getString(it) }
                    } else null  // Si pas de stock, `null`
                )

                // Ajout du magasin à la liste
                magasins.add(magasin)
            }

        } catch (e: JSONException) {
            e.printStackTrace()  // Affiche l'erreur de parsing en console
        }

        return magasins  // Retourne la liste des magasins
    }
}