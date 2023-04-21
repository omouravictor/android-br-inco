package com.omouravictor.ratesbr.presenter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.omouravictor.ratesbr.R
import com.omouravictor.ratesbr.data.Datastore.dataStore
import com.omouravictor.ratesbr.databinding.ActivityMainBinding
import com.omouravictor.ratesbr.presenter.welcome.WelcomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mainActivityBinding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment

        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.stocks_fragment,
                R.id.rates_fragment,
                R.id.bitcoins_fragment
            )
        )

        setSupportActionBar(mainActivityBinding.toolbar)
        mainActivityBinding.bottomNav.setupWithNavController(navController)
        mainActivityBinding.toolbar.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            mainActivityBinding.toolbar.isVisible = destination.id == R.id.converter_fragment
        }

        supportActionBar?.title = navController.currentDestination?.label

        checkIfIsAppFirstRun()
    }

    private fun checkIfIsAppFirstRun() {
        val appFirstRunPrefKey = booleanPreferencesKey("appFirstRun")
        val appFirstRunFlow: Flow<Boolean> = application.dataStore.data.map { preferences ->
            preferences[appFirstRunPrefKey] ?: true
        }

        lifecycleScope.launch {
            appFirstRunFlow.collect {
                if (it) {
                    val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

}