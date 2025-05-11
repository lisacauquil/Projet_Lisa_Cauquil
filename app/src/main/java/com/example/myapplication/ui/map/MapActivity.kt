package com.example.myapplication.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.myapplication.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

/**
 * Activité principale pour afficher la carte et le tracé d'itinéraire.
 */
class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    // Carte Google Map
    private lateinit var googleMap: GoogleMap

    // Service de localisation pour obtenir la position actuelle
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Éléments de l'interface utilisateur
    private lateinit var tvDuration: TextView
    private lateinit var btnDrive: Button
    private lateinit var btnWalk: Button

    // Variables pour le mode de transport et la couleur du tracé
    private var mode: String = ""
    private var polylineColor: Int = 0

    // Destination fixe : Toulouse
    private val destination = LatLng(43.6045, 1.4442)

    /**
     * Méthode appelée lors de la création de l'activité.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Initialisation des éléments d'interface
        tvDuration = findViewById(R.id.tvDuration)
        btnDrive = findViewById(R.id.btnDrive)
        btnWalk = findViewById(R.id.btnWalk)

        // Initialisation du service de localisation
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Initialisation de la carte
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Configuration des boutons
        setupButtons()
    }

    /**
     * Méthode appelée lorsque la carte est prête.
     * @param map La carte Google Map initialisée.
     */
    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Ajoute un marqueur à la position de destination (Toulouse)
        googleMap.addMarker(MarkerOptions().position(destination).title("Destination"))

        // Zoom sur la destination
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destination, 13f))
    }

    /**
     * Configuration des boutons "Voiture" et "À Pied".
     * Lorsqu'un bouton est cliqué, le mode est défini et la requête est envoyée.
     */
    private fun setupButtons() {
        btnDrive.setOnClickListener {
            mode = "driving"  // Mode voiture
            polylineColor = resources.getColor(R.color.purple_500)
            fetchRoute()
        }

        btnWalk.setOnClickListener {
            mode = "walking"  // Mode à pied
            polylineColor = resources.getColor(R.color.orange_500)
            fetchRoute()
        }
    }

    /**
     * Récupère l'itinéraire entre la position actuelle et la destination.
     * Utilise l'API Google Directions.
     */
    private fun fetchRoute() {

        // Vérifie les permissions de localisation
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }

        // Obtention de la localisation actuelle
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {

                // Coordonnées de départ (position actuelle)
                val start = LatLng(location.latitude, location.longitude)

                // URL pour la requête API Google Directions
                val url = "https://maps.googleapis.com/maps/api/directions/json?origin=${start.latitude},${start.longitude}&destination=${destination.latitude},${destination.longitude}&mode=$mode&language=fr&key=AIzaSyBJiAGj9uTDO9HfFQVuJNciN5almTzVZtY"

                // Création de la requête HTTP
                val client = OkHttpClient()
                val request = Request.Builder().url(url).build()

                // Exécution de la requête API dans un thread secondaire
                Thread {
                    try {
                        val response = client.newCall(request).execute()
                        val jsonData = response.body?.string()

                        // Vérifie que la réponse n'est pas vide
                        if (!jsonData.isNullOrEmpty()) {

                            // Analyse de la réponse JSON
                            val routes = JSONObject(jsonData).optJSONArray("routes")

                            // Si des routes sont trouvées
                            if (routes != null && routes.length() > 0) {

                                // Extraction des informations de durée et de tracé
                                val leg = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0)
                                val duration = leg.getJSONObject("duration").getString("text")
                                val polyline = routes.getJSONObject(0).getJSONObject("overview_polyline").getString("points")

                                // Mise à jour de l'interface utilisateur (UI) dans le thread principal
                                runOnUiThread {
                                    tvDuration.text = "Durée estimée : $duration"
                                    drawPolyline(polyline)  // Tracé du chemin
                                }
                            }
                        }
                    } catch (e: Exception) {
                        // En cas d'erreur, affiche un message d'erreur
                        runOnUiThread {
                            Toast.makeText(this, "Erreur de connexion", Toast.LENGTH_SHORT).show()
                        }
                    }
                }.start()

            } else {
                // Si la localisation est nulle, affiche un message
                Toast.makeText(this, "Localisation non disponible", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Trace le chemin sur la carte en utilisant le polyline encodé.
     * @param encodedPath Le tracé encodé reçu de l'API Google Directions.
     */
    private fun drawPolyline(encodedPath: String) {
        // Décodage du tracé encodé
        val path = com.google.maps.android.PolyUtil.decode(encodedPath)

        // Si le tracé est valide, l'ajouter à la carte
        if (path.isNotEmpty()) {
            googleMap.addPolyline(
                PolylineOptions()
                    .addAll(path)          // Liste des points du tracé
                    .color(polylineColor)  // Couleur selon le mode (voiture ou marche)
                    .width(8f)             // Largeur du tracé
            )
        }
    }
}