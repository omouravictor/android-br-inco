package com.omouravictor.ratesbr.presenter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.omouravictor.ratesbr.R
import com.omouravictor.ratesbr.data.DataStore.dataStore
import com.omouravictor.ratesbr.databinding.ActivityMainBinding
import com.omouravictor.ratesbr.presenter.welcome.WelcomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

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

        binding.bottomNav.setupWithNavController(navController)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        supportActionBar?.title = navController.currentDestination?.label

        checksNewUser()
    }

    private fun checksNewUser() {
        val newUserPrefKey = booleanPreferencesKey("newUser")
        val newUserDataStoreFlow: Flow<Boolean> = dataStore.data.map { it[newUserPrefKey] ?: true }

        lifecycleScope.launch {
            newUserDataStoreFlow.collect {
                if (it) {
                    val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

}