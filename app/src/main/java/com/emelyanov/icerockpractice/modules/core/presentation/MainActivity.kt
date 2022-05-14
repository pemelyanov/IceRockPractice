package com.emelyanov.icerockpractice.modules.core.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.emelyanov.icerockpractice.R
import com.emelyanov.icerockpractice.databinding.ActivityMainBinding
import com.emelyanov.icerockpractice.modules.core.domain.CoreViewModel
import com.emelyanov.icerockpractice.navigation.core.CoreNavProvider
import com.emelyanov.icerockpractice.navigation.core.launchNavHost
import com.emelyanov.icerockpractice.navigation.core.setupActionBarWithDestinations
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private val coreViewModel: CoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.coreNavContainer) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)

        launchNavHost(coreViewModel.coreNavProvider, navController)

        setupActionBarWithNavController(navController)
        setupActionBarWithDestinations(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.git_appbar, menu)
        menu?.findItem(R.id.logout_button)?.setOnMenuItemClickListener {
            coreViewModel.logoutClick()
            true
        }
        return super.onCreateOptionsMenu(menu)
    }
}