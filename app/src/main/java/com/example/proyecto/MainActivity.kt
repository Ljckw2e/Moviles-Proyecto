package com.example.proyecto

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView

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

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navView = findViewById<NavigationView>(R.id.nav_view)
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.bottom_home, R.id.nav_add_event, R.id.nav_consult_event,
                R.id.nav_calendar, R.id.nav_backup, R.id.nav_restore,
                R.id.nav_settings, R.id.nav_about
            ),
            drawerLayout
        )
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(bottomNav, navController)
        NavigationUI.setupWithNavController(navView, navController)

        bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.bottom_exit) {
                confirmarSalida()
                false
            } else {
                NavigationUI.onNavDestinationSelected(item, navController)
            }
        }
        navView.setNavigationItemSelectedListener { item ->
            if (item.itemId == R.id.nav_exit) {
                drawerLayout.closeDrawers()
                confirmarSalida()
                true
            } else {
                val ok = NavigationUI.onNavDestinationSelected(item, navController)
                if (ok) drawerLayout.closeDrawers()
                ok
            }
        }
    }

    private fun confirmarSalida() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Salir")
            .setMessage("¿Quieres cerrar la aplicación?")
            .setPositiveButton("Salir") { _, _ -> finishAffinity() }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
