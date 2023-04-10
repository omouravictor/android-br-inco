package com.omouravictor.ratesbr.presenter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
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

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.bottomNav
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_stocks, R.id.nav_conversions, R.id.nav_bitcoins
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

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