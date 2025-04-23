package com.example.myapplication.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.myapplication.R
import com.example.myapplication.data.model.Magasin
import com.example.myapplication.data.repository.MagasinRepository
import com.example.myapplication.ui.accueil.SelectPassionActivity
import com.example.myapplication.ui.list.ListFragment
import com.example.myapplication.ui.map.MapFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    private lateinit var magasinsFiltres: List<Magasin>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val passions = intent.getStringArrayListExtra("passions") ?: arrayListOf()

        val repository = MagasinRepository(applicationContext)
        val tousLesMagasins = repository.loadMagasins()
        magasinsFiltres = tousLesMagasins.filter { it.passion in passions }

        supportFragmentManager.commit {
            replace(R.id.fragment_container, MapFragment.newInstance(magasinsFiltres))
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_map -> {
                    supportFragmentManager.commit {
                        replace(R.id.fragment_container, MapFragment.newInstance(magasinsFiltres))
                    }
                    true
                }
                R.id.nav_list -> {
                    supportFragmentManager.commit {
                        replace(R.id.fragment_container, ListFragment.newInstance(magasinsFiltres))
                    }
                    true
                }
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_change_passions -> {
                val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
                prefs.edit() { remove("selected_passions") }

                val intent = Intent(this, SelectPassionActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}