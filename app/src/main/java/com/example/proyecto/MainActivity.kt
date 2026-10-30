package com.example.proyecto

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as androidx.navigation.fragment.NavHostFragment
        val navController = navHostFragment.navController
        val bottomNav = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navigation)
        androidx.navigation.ui.NavigationUI.setupWithNavController(bottomNav, navController)
        val navView = findViewById<com.google.android.material.navigation.NavigationView>(R.id.nav_view)
        androidx.navigation.ui.NavigationUI.setupWithNavController(navView, navController)
        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.topAppBar)
        val drawerLayout = findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.drawer_layout)
        val appBarConfiguration = androidx.navigation.ui.AppBarConfiguration(
            setOf(R.id.bottom_home, R.id.nav_add_event, R.id.nav_consult_event),
            drawerLayout
        )
        androidx.navigation.ui.NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)
    }
}