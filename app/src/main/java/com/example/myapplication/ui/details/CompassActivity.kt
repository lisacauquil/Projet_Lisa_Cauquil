package com.example.myapplication.ui.details

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.data.model.Magasin

class CompassActivity : AppCompatActivity() {

    private lateinit var textDistance: TextView
    private lateinit var textDirection: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compass)

        val magasin = intent.getSerializableExtra("magasin") as? Magasin

        textDistance = findViewById(R.id.text_distance)
        textDirection = findViewById(R.id.text_direction)

        magasin?.let {
            textDistance.text = "Distance vers ${it.nom} : [à calculer]"
            textDirection.text = "Direction : [à calculer]"
        }
    }
}
