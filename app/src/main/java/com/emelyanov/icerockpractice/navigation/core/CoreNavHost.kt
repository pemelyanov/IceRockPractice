package com.emelyanov.icerockpractice.navigation.core

import android.app.Activity
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHost
import com.emelyanov.icerockpractice.R
import com.emelyanov.icerockpractice.modules.core.presentation.MainActivity

fun MainActivity.launchNavHost(
    coreNavProvider: CoreNavProvider,
    coreNavController: NavController
) {
    coreNavProvider.observeNavigationFlow(lifecycleScope) {
        when(it) {
            is CoreDestinations.Authentication -> coreNavController.navigate(R.id.navigateToAuth)
            is CoreDestinations.RepositoriesList -> coreNavController.navigate(R.id.navigateToList)
            is CoreDestinations.RepositoryDetails -> {
                coreNavController.navigate(R.id.navigateToDetails)
            }
        }
    }
}

fun MainActivity.setupActionBarWithDestinations(
    coreNavController: NavController
) {
    coreNavController.addOnDestinationChangedListener { _, destination, _ ->
        if(destination.id == R.id.authorizationFragment) supportActionBar?.hide() else supportActionBar?.show()
        if(destination.id == R.id.repositoriesListFragment) supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}