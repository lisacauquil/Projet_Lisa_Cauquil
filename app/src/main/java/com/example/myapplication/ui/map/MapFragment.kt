package com.example.myapplication.ui.map

import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.data.model.Magasin
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.Serializable

/**
 * Fragment qui affiche une carte avec des marqueurs représentant les magasins.
 */
class MapFragment : Fragment(), OnMapReadyCallback {

    // Composant de la carte
    private lateinit var mapView: MapView

    // Liste des magasins à afficher
    private lateinit var magasins: List<Magasin>

    // Instance de la carte Google Map
    private var googleMap: GoogleMap? = null

    /**
     * Initialisation du fragment.
     * Récupère la liste des magasins envoyée via les arguments.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Récupération des magasins depuis les arguments
        magasins = arguments?.getSerializable("magasins") as List<Magasin>
    }

    /**
     * Crée et renvoie la vue du fragment.
     * Initialise la carte.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        // Initialisation de la MapView
        mapView = MapView(requireContext())
        (view.findViewById<ViewGroup>(R.id.map_container)).addView(mapView)

        // Création de la carte
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return view
    }

    /**
     * Méthode appelée lorsque la carte est prête à être utilisée.
     * @param map L'instance de la carte Google Map.
     */
    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Si la liste des magasins n'est pas vide, on centre la carte sur le premier magasin
        if (magasins.isNotEmpty()) {
            val premierMagasin = LatLng(magasins[0].latitude, magasins[0].longitude)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(premierMagasin, 13f))
        }

        // Parcours de tous les magasins pour ajouter un marqueur sur la carte
        for (magasin in magasins) {
            val position = LatLng(magasin.latitude, magasin.longitude)

            // Création du marqueur avec une couleur personnalisée selon la passion
            map.addMarker(
                MarkerOptions()
                    .position(position)
                    .title(magasin.nom)
                    .icon(getMarkerColorForPassion(magasin.passion))  // Appel de la méthode pour obtenir la couleur
            )
        }
    }

    /**
     * Méthode qui retourne une icône de marqueur colorée en fonction de la passion du magasin.
     * @param passion La passion associée au magasin.
     * @return Un `BitmapDescriptor` représentant l'icône du marqueur.
     */
    private fun getMarkerColorForPassion(passion: String): BitmapDescriptor {

        // Définition des couleurs selon la passion
        val color = when (passion.lowercase()) {
            "montres" -> 0xFFE91E63.toInt()  // Rose
            "sport" -> 0xFF2196F3.toInt()    // Bleu
            "lecture" -> 0xFF4CAF50.toInt()  // Vert
            else -> 0xFF757575.toInt()       // Gris par défaut
        }

        // Création d'un cercle coloré
        val paint = Paint().apply {
            this.color = color
            this.style = Paint.Style.FILL
        }

        // Taille et centre du cercle
        val radius = 20f
        val bitmap = Bitmap.createBitmap(40, 40, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Dessine le cercle
        canvas.drawCircle(20f, 20f, radius, paint)

        // Crée un `BitmapDescriptor` à partir du bitmap généré
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    /**
     * Méthode statique pour créer une nouvelle instance du fragment avec une liste de magasins.
     * @param magasins La liste des magasins à afficher.
     * @return Une nouvelle instance de `MapFragment`.
     */
    companion object {
        fun newInstance(magasins: List<Magasin>): MapFragment {
            val fragment = MapFragment()
            val args = Bundle()

            // Envoie des magasins sous forme de `Serializable`
            args.putSerializable("magasins", magasins as Serializable)
            fragment.arguments = args
            return fragment
        }
    }

    /**
     * Gestion des cycles de vie pour la `MapView`.
     * Ces méthodes sont nécessaires pour que la carte fonctionne correctement.
     */
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}