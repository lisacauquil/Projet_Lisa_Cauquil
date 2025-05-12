package com.example.myapplication.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

/**
 * MapActivity
 * Cette activité affiche une carte centrée sur la localisation actuelle et permet
 * de tracer un itinéraire vers une destination prédéfinie.
 */
class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    // Carte Google Map
    private lateinit var googleMap: GoogleMap

    // Service de localisation
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Éléments d'interface utilisateur
    private lateinit var btnDrive: Button
    private lateinit var btnWalk: Button
    private lateinit var tvDuration: TextView

    // Code de la permission pour la localisation
    private val LOCATION_PERMISSION_REQUEST = 1001

    // Mode de transport et couleur du tracé
    private var mode: String = ""
    private var polylineColor: Int = 0

    // Destination : Capitole, Toulouse (Lat, Lng)
    private val destination = LatLng(43.6045, 1.4442)

    /**
     * Méthode appelée lors de la création de l'activité.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Initialisation des composants d'interface
        btnDrive = findViewById(R.id.btnDrive)
        btnWalk = findViewById(R.id.btnWalk)
        tvDuration = findViewById(R.id.tvDuration)

        // Initialisation du service de localisation
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Initialisation de la carte
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Configuration des boutons
        setupButtons()
    }

    /**
     * Méthode appelée lorsque la carte est prête à être utilisée.
     */
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        Log.d("MapActivity", "Carte initialisée")

        // Active la localisation de l'utilisateur
        enableMyLocation()

        // Ajoute un marqueur à la destination
        googleMap.addMarker(
            MarkerOptions()
                .position(destination)
                .title("Magasin")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        )
    }

    /**
     * Active la localisation de l'utilisateur et centre la carte sur sa position.
     */
    private fun enableMyLocation() {
        // Vérifie si la permission de localisation est accordée
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Active le bouton de localisation sur la carte
            googleMap.isMyLocationEnabled = true

            // Récupère la localisation actuelle
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val userLatLng = LatLng(location.latitude, location.longitude)
                    Log.d("MapActivity", "Localisation actuelle : ${location.latitude}, ${location.longitude}")

                    // Centre la carte sur la localisation actuelle
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))

                    // Ajoute un marqueur pour la localisation actuelle
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(userLatLng)
                            .title("Vous êtes ici")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    )

                } else {
                    Log.e("MapActivity", "Localisation non disponible")
                    Toast.makeText(this, "Localisation non disponible", Toast.LENGTH_SHORT).show()
                }
            }

        } else {
            // Demande la permission de localisation
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST)
        }
    }

    /**
     * Gère la réponse de la demande de permission.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation()
            } else {
                Toast.makeText(this, "Permission de localisation refusée", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Configuration des boutons "Voiture" et "À pied".
     */
    private fun setupButtons() {
        btnDrive.setOnClickListener {
            mode = "driving"
            polylineColor = resources.getColor(R.color.purple_500)
            fetchRoute()
        }

        btnWalk.setOnClickListener {
            mode = "walking"
            polylineColor = resources.getColor(R.color.orange_500)
            fetchRoute()
        }
    }

    /**
     * Récupère l'itinéraire entre la position actuelle et la destination.
     */
    private fun fetchRoute() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        // Récupère la localisation actuelle
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val start = LatLng(location.latitude, location.longitude)

                val url = "https://maps.googleapis.com/maps/api/directions/json?origin=${start.latitude},${start.longitude}&destination=${destination.latitude},${destination.longitude}&mode=$mode&language=fr&key=AIzaSyBJiAGj9uTDO9HfFQVuJNciN5almTzVZtY"
                sendRequest(url)

            } else {
                Toast.makeText(this, "Localisation non disponible", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Envoie la requête à l'API Directions pour obtenir le tracé et la durée.
     */
    private fun sendRequest(url: String) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        Thread {
            try {
                val response = client.newCall(request).execute()
                val jsonData = response.body?.string()

                if (!jsonData.isNullOrEmpty()) {
                    val routes = JSONObject(jsonData).optJSONArray("routes")
                    if (routes != null && routes.length() > 0) {
                        val leg = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0)
                        val duration = leg.getJSONObject("duration").getString("text")
                        val polyline = routes.getJSONObject(0).getJSONObject("overview_polyline").getString("points")

                        // Met à jour l'interface utilisateur
                        runOnUiThread {
                            tvDuration.text = "Durée estimée : $duration"
                            drawPolyline(polyline)
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this, "Aucun itinéraire trouvé", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            } catch (e: Exception) {
                Log.e("MapActivity", "Erreur : ${e.message}")
            }
        }.start()
    }

    /**
     * Trace le chemin sur la carte.
     */
    private fun drawPolyline(encodedPath: String) {
        val path = com.google.maps.android.PolyUtil.decode(encodedPath)
        if (path.isNotEmpty()) {
            googleMap.addPolyline(
                PolylineOptions()
                    .addAll(path)
                    .color(polylineColor)
                    .width(8f)
            )
        }
    }
}