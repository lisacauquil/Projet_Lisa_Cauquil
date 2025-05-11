package com.example.myapplication.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit
import androidx.fragment.app.commit
import com.example.myapplication.R
import com.example.myapplication.data.model.Magasin
import com.example.myapplication.data.repository.MagasinRepository
import com.example.myapplication.ui.accueil.SelectPassionActivity
import com.example.myapplication.ui.list.ListFragment
import com.example.myapplication.ui.map.MapFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Activité principale de l'application.
 * Gère l'affichage des fragments (Carte / Liste) et la navigation.
 */
class MainActivity : AppCompatActivity() {

    // Liste des magasins filtrés par passions
    private lateinit var magasinsFiltres: List<Magasin>

    /**
     * Méthode appelée lors de la création de l'activité.
     * Initialise la Toolbar, la Bottom Navigation et les fragments.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialisation de la Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        /**
         * Récupération des passions sélectionnées.
         * Les passions sont envoyées depuis `SelectPassionActivity`.
         */
        val passions = intent.getStringArrayListExtra("passions") ?: arrayListOf()

        /**
         * Chargement des magasins depuis le repository.
         * Les magasins sont filtrés par passion.
         */
        val repository = MagasinRepository(this)
        val tousLesMagasins = repository.getMagasins()
        magasinsFiltres = tousLesMagasins.filter { it.passion in passions }

        /**
         * Initialisation du fragment par défaut (`MapFragment`).
         * Affiche la carte des magasins filtrés.
         */
        supportFragmentManager.commit {
            replace(R.id.fragment_container, MapFragment.newInstance(ArrayList(magasinsFiltres)))
        }

        /**
         * Gestion de la navigation avec le Bottom Navigation.
         * Permet de basculer entre la carte (`MapFragment`) et la liste (`ListFragment`).
         */
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                // Navigation vers le fragment de la carte
                R.id.nav_map -> {
                    supportFragmentManager.commit {
                        replace(R.id.fragment_container, MapFragment.newInstance(ArrayList(magasinsFiltres)))
                    }
                    true
                }

                // Navigation vers le fragment de la liste
                R.id.nav_list -> {
                    supportFragmentManager.commit {
                        replace(R.id.fragment_container, ListFragment.newInstance(ArrayList(magasinsFiltres)))
                    }
                    true
                }
                else -> false
            }
        }
    }

    /**
     * Méthode pour initialiser le menu d'options.
     * @param menu Le menu à afficher.
     * @return `true` si le menu est créé.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    /**
     * Méthode appelée lorsque l'utilisateur sélectionne un élément du menu.
     * Gère le changement de passions.
     * @param item L'élément sélectionné.
     * @return `true` si l'élément est géré.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            // Action pour changer les passions sélectionnées
            R.id.action_change_passions -> {

                // Suppression des passions stockées dans les préférences
                val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
                prefs.edit { remove("selected_passions") }

                // Redirection vers `SelectPassionActivity` pour re-sélectionner les passions
                val intent = Intent(this, SelectPassionActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                true
            }

            // Si l'élément sélectionné n'est pas géré
            else -> super.onOptionsItemSelected(item)
        }
    }
}