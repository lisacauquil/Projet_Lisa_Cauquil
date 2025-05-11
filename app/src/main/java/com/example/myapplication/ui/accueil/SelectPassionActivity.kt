package com.example.myapplication.ui.accueil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.ui.main.MainActivity
import androidx.core.content.edit

/**
 * Activité pour sélectionner les passions.
 * Les passions sélectionnées sont sauvegardées et envoyées à `MainActivity`.
 */
class SelectPassionActivity : AppCompatActivity() {

    // Références aux éléments de l'interface utilisateur
    private lateinit var recyclerView: RecyclerView
    private lateinit var buttonContinue: Button

    // Liste des passions disponibles
    private val passions = listOf("Montres", "Sport", "Lecture")

    // Liste des passions sélectionnées par l'utilisateur
    private val selectedPassions = mutableListOf<String>()

    // Adaptateur pour le RecyclerView
    private lateinit var adapter: PassionAdapter

    /**
     * Méthode appelée lors de la création de l'activité.
     * Initialise les éléments d'interface et configure le RecyclerView.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_passion)

        // Initialisation des éléments d'interface
        recyclerView = findViewById(R.id.recycler_passions)
        buttonContinue = findViewById(R.id.button_continue)

        /**
         * Création de l'adaptateur `PassionAdapter`.
         * Le callback `onCheckChanged` est utilisé pour ajouter ou retirer des passions sélectionnées.
         */
        adapter = PassionAdapter(passions) { passion, isChecked ->
            if (isChecked) selectedPassions.add(passion)
            else selectedPassions.remove(passion)
        }

        // Configuration du RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Récupération des passions déjà sélectionnées (si présentes)
        val passionsDéjàCochées = loadSavedPassions()
        adapter.setPreSelectedPassions(passionsDéjàCochées)
        selectedPassions.addAll(passionsDéjàCochées)

        /**
         * Gestion du clic sur le bouton "Continuer".
         * - Sauvegarde des passions sélectionnées.
         * - Redirige vers `MainActivity`.
         */
        buttonContinue.setOnClickListener {
            saveSelectedPassions(selectedPassions)

            // Création de l'Intent pour lancer MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.putStringArrayListExtra("passions", ArrayList(selectedPassions))
            startActivity(intent)

            // Termine cette activité pour éviter de revenir en arrière
            finish()
        }
    }

    /**
     * Sauvegarde les passions sélectionnées dans les préférences partagées.
     * @param passions Liste des passions sélectionnées.
     */
    private fun saveSelectedPassions(passions: List<String>) {
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)

        // Conversion en Set pour la sauvegarde
        prefs.edit {
            putStringSet("selected_passions", passions.toSet())
        }
    }

    /**
     * Charge les passions sélectionnées depuis les préférences partagées.
     * @return Liste des passions déjà sélectionnées ou liste vide.
     */
    private fun loadSavedPassions(): List<String> {
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)

        // Récupère le Set des passions sélectionnées ou un Set vide par défaut
        return prefs.getStringSet("selected_passions", emptySet())!!.toList()
    }
}