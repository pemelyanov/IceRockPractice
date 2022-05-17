package com.emelyanov.icerockpractice.modules.core.presentation

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.emelyanov.icerockpractice.R
import com.emelyanov.icerockpractice.databinding.ActivityMainBinding
import com.emelyanov.icerockpractice.modules.core.domain.CoreViewModel
import com.emelyanov.icerockpractice.navigation.core.launchNavHost
import com.emelyanov.icerockpractice.navigation.core.setupActionBarWithDestinations
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private val coreViewModel: CoreViewModel by viewModels()
    private var destinationListener: NavController.OnDestinationChangedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_IceRockPractice)
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                coreViewModel.actions.collect {
                    when(it) {
                        is CoreViewModel.Action.ShowExitDialog -> {
                            AlertDialog.Builder(this@MainActivity, R.style.GitAlertDialog)
                                .setTitle(getString(R.string.logout_dialog_title))
                                .setMessage(getString(R.string.logout_dialog_message))
                                .setPositiveButton(getString(R.string.logout_dialog_positive_button)) { _, _ ->
                                    it.onSuccess()
                                }
                                .setNegativeButton(getString(R.string.logout_dialog_negative_button)) { _, _ ->
                                }
                                .create()
                                .show()
                        }
                    }
                }
            }
        }

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
            coreViewModel.onLogoutClick()
            true
        }
        return super.onCreateOptionsMenu(menu)
    }
}