package com.emelyanov.icerockpractice.navigation.core

import android.app.Activity
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHost
import com.emelyanov.icerockpractice.R
import com.emelyanov.icerockpractice.modules.core.presentation.MainActivity
import com.emelyanov.icerockpractice.modules.repos.modules.details.presentation.DetailInfoFragment
import kotlinx.coroutines.launch

/**
 * Launches destination observing.
 * @param coreNavProvider - input nav provider for observing
 * @param coreNavController - input nav controller for routing
 */
fun MainActivity.launchNavHost(
    coreNavProvider: CoreNavProvider,
    coreNavController: NavController
) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            coreNavProvider.observeNavigationFlow(this@repeatOnLifecycle) { destination ->
                when (destination) {
                    is CoreDestinations.Authentication -> coreNavController.navigate(R.id.navigateToAuth)
                    is CoreDestinations.RepositoriesList -> coreNavController.navigate(R.id.navigateToList)
                    is CoreDestinations.RepositoryDetails -> {
                        coreNavController.navigate(
                            R.id.navigateToDetails,
                            DetailInfoFragment.createArguments(
                                destination.owner,
                                destination.repo
                            )
                        )
                    }
                }
            }
        }
    }
}

/**
 * Setups action bar with nav controller destinations for hiding some elements on specific routes
 */
fun MainActivity.setupActionBarWithDestinations(
    coreNavController: NavController
) {
    coreNavController.addOnDestinationChangedListener { _, destination, _ ->
        if(destination.id == R.id.authorizationFragment) supportActionBar?.hide() else supportActionBar?.show()
        if(destination.id == R.id.repositoriesListFragment) supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}