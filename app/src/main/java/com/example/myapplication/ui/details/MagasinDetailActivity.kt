package com.example.myapplication.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.data.model.Magasin

class MagasinDetailActivity : AppCompatActivity() {

    private var magasin: Magasin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_magasin_detail)

        val buttonRetour = findViewById<Button>(R.id.button_retour)
        val buttonItineraire = findViewById<Button>(R.id.button_itineraire)
        val nom = findViewById<TextView>(R.id.text_nom_magasin)
        val adresse = findViewById<TextView>(R.id.text_adresse_magasin)
        val stock = findViewById<TextView>(R.id.text_stock_magasin)

        magasin = intent.getSerializableExtra("magasin") as? Magasin

        magasin?.let {
            nom.text = it.nom
            adresse.text = it.adresse

            if (it.partenaire && it.stock != null) {
                stock.text = "Stock disponible :\n" + it.stock.joinToString("\n")
            } else {
                stock.text = "Non partenaire ou stock indisponible"
            }
        }

        buttonRetour.setOnClickListener {
            finish()
        }

        buttonItineraire.setOnClickListener {
            magasin?.let {
                val gmmIntentUri = Uri.parse("geo:${it.latitude},${it.longitude}?q=${it.latitude},${it.longitude}(${Uri.encode(it.nom)})")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                if (mapIntent.resolveActivity(packageManager) != null) {
                    startActivity(mapIntent)
                }
            }
        }
    }
}
