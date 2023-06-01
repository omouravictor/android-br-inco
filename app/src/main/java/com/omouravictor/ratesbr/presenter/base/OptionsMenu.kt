package com.omouravictor.ratesbr.presenter.base

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.omouravictor.ratesbr.R

class OptionsMenu {

    lateinit var searchMenuItem: MenuItem

    fun addOptionsMenu(
        fragmentActivity: FragmentActivity,
        viewLifecycleOwner: LifecycleOwner,
        callbackFunction: (String) -> Unit
    ) {
        fragmentActivity.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_options, menu)
                searchMenuItem = menu.findItem(R.id.searchMenu)

                val searchView = searchMenuItem.actionView as SearchView

                searchView.queryHint = fragmentActivity.baseContext.getString(R.string.search)
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(text: String): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(text: String): Boolean {
                        callbackFunction(text)
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean = false
        }, viewLifecycleOwner, Lifecycle.State.STARTED)
    }

}