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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_magasin_detail)

        val magasin = intent.getSerializableExtra("magasin") as? Magasin

        val textNom = findViewById<TextView>(R.id.text_nom)
        val textAdresse = findViewById<TextView>(R.id.text_adresse)
        val textStock = findViewById<TextView>(R.id.text_stock)
        val btnMap = findViewById<Button>(R.id.button_itineraire)
        val btnCompass = findViewById<Button>(R.id.button_boussole)

        magasin?.let {
            textNom.text = it.nom
            textAdresse.text = it.adresse
            textStock.text = if (it.partenaire) it.stock?.joinToString("\n") ?: "" else "Non partenaire"

            btnMap.setOnClickListener {
                val uri = "google.navigation:q=${magasin.latitude},${magasin.longitude}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                intent.setPackage("com.google.android.apps.maps")
                startActivity(intent)
            }

            btnCompass.setOnClickListener {
                val intent = Intent(this, CompassActivity::class.java)
                intent.putExtra("magasin", magasin)
                startActivity(intent)
            }
        }
    }
}
