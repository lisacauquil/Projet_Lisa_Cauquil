package com.example.myapplication.ui.accueil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

/**
 * Adaptateur pour afficher la liste des passions sous forme de `CheckBox`.
 * Permet à l'utilisateur de sélectionner ou désélectionner des passions.
 *
 * @param passions Liste des passions à afficher.
 * @param onCheckChanged Callback déclenché lorsqu'une passion est cochée/décochée.
 */
class PassionAdapter(
    private val passions: List<String>,   // Liste des passions à afficher
    private val onCheckChanged: (String, Boolean) -> Unit   // Callback pour les changements d'état des cases
) : RecyclerView.Adapter<PassionAdapter.PassionViewHolder>() {

    // Ensemble des passions déjà sélectionnées
    private var preSelected: Set<String> = emptySet()

    /**
     * Met à jour les passions pré-sélectionnées.
     * @param passions Liste des passions sélectionnées.
     */
    fun setPreSelectedPassions(passions: List<String>) {
        preSelected = passions.toSet()

        // Notifie le RecyclerView que les données ont changé pour mettre à jour les CheckBox
        notifyDataSetChanged()
    }

    /**
     * Crée le ViewHolder en gonflant le layout `item_passion.xml`.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_passion, parent, false)
        return PassionViewHolder(view)
    }

    /**
     * Associe les données à la vue.
     * Configure la `CheckBox` pour chaque passion.
     */
    override fun onBindViewHolder(holder: PassionViewHolder, position: Int) {
        val passion = passions[position]

        // Définit le texte de la CheckBox avec le nom de la passion
        holder.checkBox.text = passion

        // Empêche la propagation d'événements de changement d'état pendant la configuration
        holder.checkBox.setOnCheckedChangeListener(null)

        // Met à jour l'état de la CheckBox (cochée ou non)
        holder.checkBox.isChecked = preSelected.contains(passion)

        /**
         * Réagit aux changements d'état de la CheckBox.
         * - `isChecked` indique si la CheckBox est cochée ou non.
         */
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onCheckChanged(passion, isChecked)
        }
    }

    /**
     * Retourne le nombre total d'éléments dans la liste des passions.
     */
    override fun getItemCount(): Int = passions.size

    /**
     * ViewHolder pour l'adaptateur des passions.
     * Contient une `CheckBox`.
     */
    class PassionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Référence à la CheckBox
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxPassion)
    }
}