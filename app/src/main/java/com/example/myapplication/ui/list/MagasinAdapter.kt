package com.example.myapplication.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Magasin

class MagasinAdapter(
    private val magasins: List<Magasin>,
    private val onClick: (Magasin) -> Unit
) : RecyclerView.Adapter<MagasinAdapter.MagasinViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MagasinViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_magasin, parent, false)
        return MagasinViewHolder(view)
    }

    override fun onBindViewHolder(holder: MagasinViewHolder, position: Int) {
        val magasin = magasins[position]
        holder.nom.text = magasin.nom
        holder.adresse.text = magasin.adresse
        holder.itemView.setOnClickListener { onClick(magasin) }
    }

    override fun getItemCount(): Int = magasins.size

    class MagasinViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nom: TextView = view.findViewById(R.id.nom_magasin)
        val adresse: TextView = view.findViewById(R.id.adresse_magasin)
    }
}