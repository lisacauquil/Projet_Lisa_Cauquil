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

class SelectPassionActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var buttonContinue: Button

    private val passions = listOf("Montres", "Sport", "Lecture")
    private val selectedPassions = mutableListOf<String>()
    private lateinit var adapter: PassionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_passion)

        recyclerView = findViewById(R.id.recycler_passions)
        buttonContinue = findViewById(R.id.button_continue)

        adapter = PassionAdapter(passions) { passion, isChecked ->
            if (isChecked) selectedPassions.add(passion)
            else selectedPassions.remove(passion)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val passionsDéjàCochées = loadSavedPassions()
        adapter.setPreSelectedPassions(passionsDéjàCochées)
        selectedPassions.addAll(passionsDéjàCochées)

        buttonContinue.setOnClickListener {
            saveSelectedPassions(selectedPassions)

            val intent = Intent(this, MainActivity::class.java)
            intent.putStringArrayListExtra("passions", ArrayList(selectedPassions))
            startActivity(intent)
            finish()
        }
    }

    private fun saveSelectedPassions(passions: List<String>) {
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        prefs.edit() { putStringSet("selected_passions", passions.toSet()) }
    }

    private fun loadSavedPassions(): List<String> {
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        return prefs.getStringSet("selected_passions", emptySet())!!.toList()
    }
}
