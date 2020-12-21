package com.example.myshop

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

class DashboardActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar!!.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this@DashboardActivity,
                R.drawable.app_gradient_color_background
            )
        )
        // END

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_products,
                R.id.navigation_dashboard,
                R.id.navigation_orders
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)
    }
    override fun onBackPressed()
    {
        doubleBackToExit()
    }
}