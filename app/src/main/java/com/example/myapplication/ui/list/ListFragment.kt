package com.example.myapplication.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Magasin
import com.example.myapplication.ui.adapter.MagasinAdapter

/**
 * Fragment qui affiche la liste des magasins dans un `RecyclerView`.
 */
class ListFragment : Fragment() {

    // RecyclerView pour afficher les magasins
    private lateinit var magasinRecyclerView: RecyclerView

    // Adaptateur pour le RecyclerView
    private lateinit var magasinAdapter: MagasinAdapter

    // Liste des magasins à afficher
    private var magasins = arrayListOf<Magasin>()

    /**
     * Méthode statique pour créer une nouvelle instance du fragment avec une liste de magasins.
     * @param magasins Liste des magasins à afficher.
     * @return Une instance de `ListFragment`.
     */
    companion object {
        fun newInstance(magasins: ArrayList<Magasin>): ListFragment {
            val fragment = ListFragment()

            // Crée un `Bundle` pour transmettre les magasins au fragment
            val bundle = Bundle()
            bundle.putSerializable("magasins", magasins)

            // Attache le `Bundle` au fragment
            fragment.arguments = bundle
            return fragment
        }
    }

    /**
     * Méthode appelée lors de la création du fragment.
     * Récupère les magasins envoyés via les arguments.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Vérifie si des arguments ont été transmis
        arguments?.let {
            // Récupère la liste des magasins
            magasins = it.getSerializable("magasins") as ArrayList<Magasin>
        }
    }

    /**
     * Crée et renvoie la vue du fragment.
     * Initialise le RecyclerView et son adaptateur.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Crée la vue à partir du layout `fragment_list.xml`
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        // Référence au RecyclerView dans le layout
        magasinRecyclerView = view.findViewById(R.id.recyclerViewMagasins)

        // Définit le layout du RecyclerView comme un `LinearLayoutManager` (liste verticale)
        magasinRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Crée l'adaptateur et l'associe au RecyclerView
        magasinAdapter = MagasinAdapter(requireContext(), magasins)
        magasinRecyclerView.adapter = magasinAdapter

        return view
    }
}