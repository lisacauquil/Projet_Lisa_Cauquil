package com.example.myapplication.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.data.model.Magasin
import com.example.myapplication.ui.map.MapActivity

/**
 * Activité d'affichage des détails d'un magasin.
 * Affiche les informations du magasin sélectionné et permet de lancer l'itinéraire.
 */
class MagasinDetailActivity : AppCompatActivity() {

    // Éléments de l'interface utilisateur
    private lateinit var tvNom: TextView
    private lateinit var tvAdresse: TextView
    private lateinit var tvPassion: TextView
    private lateinit var tvStock: TextView
    private lateinit var btnItineraire: Button

    // Magasin reçu via l'intent
    private var magasin: Magasin? = null

    /**
     * Méthode appelée lors de la création de l'activité.
     * Initialise l'interface utilisateur et affiche les informations du magasin.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_magasin_detail)

        // Initialisation des éléments d'interface
        tvNom = findViewById(R.id.tvMagasinNom)
        tvAdresse = findViewById(R.id.tvMagasinAdresse)
        tvPassion = findViewById(R.id.tvMagasinPassion)
        tvStock = findViewById(R.id.tvMagasinStock)
        btnItineraire = findViewById(R.id.btnItineraire)

        // Récupération du magasin depuis l'intent
        magasin = intent.getSerializableExtra("magasin") as? Magasin

        // Si le magasin est valide, on remplit les informations
        magasin?.let { magasin ->

            // Affichage du nom et de l'adresse du magasin
            tvNom.text = magasin.nom
            tvAdresse.text = magasin.adresse

            // Affichage de la passion associée au magasin
            tvPassion.text = "Passion : ${magasin.passion}"

            // Si le magasin est partenaire, on affiche le stock
            tvStock.text = if (magasin.partenaire) {
                "Stock disponible : ${magasin.stock?.joinToString(", ")}"
            } else {
                "Pas de stock disponible"
            }

            /**
             * Gestion du clic sur le bouton "Itinéraire".
             * Lance `MapActivity` en envoyant les coordonnées du magasin.
             */
            btnItineraire.setOnClickListener {
                val intent = Intent(this, MapActivity::class.java)

                // Ajoute les coordonnées du magasin à l'intent
                intent.putExtra("latitude", magasin.latitude)
                intent.putExtra("longitude", magasin.longitude)

                // Démarre l'activité MapActivity
                startActivity(intent)
            }
        }
    }
}