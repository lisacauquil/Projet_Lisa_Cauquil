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

class ListFragment : Fragment() {

    private lateinit var magasins: List<Magasin>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        magasins = arguments?.getSerializable("magasins") as List<Magasin>
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_magasin)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = MagasinAdapter(magasins) { magasin ->
        }

        return view
    }

    companion object {
        fun newInstance(magasins: List<Magasin>): ListFragment {
            val fragment = ListFragment()
            val args = Bundle()
            args.putSerializable("magasins", magasins as java.io.Serializable)
            fragment.arguments = args
            return fragment
        }
    }
}