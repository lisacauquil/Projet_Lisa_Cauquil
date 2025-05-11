package com.example.myapplication.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Magasin
import com.example.myapplication.ui.detail.MagasinDetailActivity

/**
 * Adaptateur pour la liste des magasins.
 * Gère l'affichage des magasins dans un `RecyclerView`.
 */
class MagasinAdapter(
    private val context: Context,   // Contexte de l'activité ou du fragment
    private val magasins: List<Magasin>   // Liste des magasins à afficher
) : RecyclerView.Adapter<MagasinAdapter.MagasinViewHolder>() {

    /**
     * ViewHolder pour chaque élément du RecyclerView.
     * Gère l'affichage du nom du magasin et le clic pour afficher les détails.
     */
    inner class MagasinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Référence à l'élément TextView pour le nom du magasin
        val tvNom: TextView = itemView.findViewById(R.id.tvMagasinNom)

        init {
            /**
             * Gestion du clic sur l'élément de la liste.
             * Lorsqu'un magasin est cliqué, lance `MagasinDetailActivity`.
             */
            itemView.setOnClickListener {
                val position = adapterPosition

                // Vérifie que la position est valide
                if (position != RecyclerView.NO_POSITION) {
                    val magasin = magasins[position]

                    // Création de l'intent pour lancer l'activité de détails
                    val intent = Intent(context, MagasinDetailActivity::class.java)

                    // Envoie le magasin sélectionné à l'activité
                    intent.putExtra("magasin", magasin)

                    // Démarre l'activité des détails
                    context.startActivity(intent)
                }
            }
        }
    }

    /**
     * Méthode appelée pour créer le ViewHolder.
     * @param parent Le ViewGroup parent.
     * @param viewType Le type de vue (ici, il n'y en a qu'un).
     * @return Un nouvel objet `MagasinViewHolder`.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MagasinViewHolder {
        // Crée la vue à partir du layout `item_magasin.xml`
        val view = LayoutInflater.from(context).inflate(R.layout.item_magasin, parent, false)
        return MagasinViewHolder(view)
    }

    /**
     * Méthode appelée pour lier les données à la vue.
     * @param holder Le ViewHolder à mettre à jour.
     * @param position La position de l'élément dans la liste.
     */
    override fun onBindViewHolder(holder: MagasinViewHolder, position: Int) {
        val magasin = magasins[position]

        // Définit le nom du magasin dans le TextView
        holder.tvNom.text = magasin.nom
    }

    /**
     * Retourne le nombre total d'éléments dans la liste.
     * @return Le nombre de magasins.
     */
    override fun getItemCount(): Int = magasins.size
}