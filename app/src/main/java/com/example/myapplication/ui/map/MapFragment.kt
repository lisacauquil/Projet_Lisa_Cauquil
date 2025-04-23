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

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var magasins: List<Magasin>
    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        magasins = arguments?.getSerializable("magasins") as List<Magasin>
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = MapView(requireContext())
        (view.findViewById<ViewGroup>(R.id.map_container)).addView(mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        return view
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        if (magasins.isNotEmpty()) {
            val premier = LatLng(magasins[0].latitude, magasins[0].longitude)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(premier, 13f))
        }

        for (magasin in magasins) {
            val position = LatLng(magasin.latitude, magasin.longitude)
            map.addMarker(
                MarkerOptions()
                    .position(position)
                    .title(magasin.nom)
                    .icon(getMarkerColorForPassion(magasin.passion))
            )
        }
    }

    private fun getMarkerColorForPassion(passion: String): BitmapDescriptor {
        val color = when (passion.lowercase()) {
            "montres" -> 0xFFE91E63.toInt()
            "sport" -> 0xFF2196F3.toInt()
            "lecture" -> 0xFF4CAF50.toInt()
            else -> 0xFF757575.toInt()
        }

        val paint = Paint()
        paint.color = color
        paint.style = Paint.Style.FILL

        val radius = 20f
        val bitmap = Bitmap.createBitmap(40, 40, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawCircle(20f, 20f, radius, paint)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    companion object {
        fun newInstance(magasins: List<Magasin>): MapFragment {
            val fragment = MapFragment()
            val args = Bundle()
            args.putSerializable("magasins", magasins as Serializable)
            fragment.arguments = args
            return fragment
        }
    }

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