package com.grittydude.brainscrambla.activities
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.grittydude.brainscrambla.R
import com.grittydude.brainscrambla.databinding.ActivityLandingPageBinding


class LandingPage : AppCompatActivity() {

    private lateinit var binding: ActivityLandingPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityLandingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //actionBar?.hide()
        supportActionBar?.hide()
        val navController: NavController = Navigation.findNavController(this, R.id.navHost)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        setupWithNavController(bottomNavigationView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.homeFragment || destination.id == R.id.playSquashFragment || destination.id == R.id.menuFragment) {

                bottomNavigationView.visibility = View.VISIBLE
            } else {

                bottomNavigationView.visibility = View.GONE
            }
        }

    }

    override fun onBackPressed() {

    }
}