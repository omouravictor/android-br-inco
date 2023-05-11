package com.omouravictor.ratesbr.util

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.omouravictor.ratesbr.R

object FragmentUtils {

    fun addSearchMenu(
        fragmentActivity: FragmentActivity,
        viewLifecycleOwner: LifecycleOwner,
        callBack: (String) -> Unit
    ) {
        fragmentActivity.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.options_menu, menu)
                val searchView = menu.findItem(R.id.searchMenu).actionView as SearchView

                searchView.queryHint = fragmentActivity.baseContext.getString(R.string.search)
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(text: String): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(text: String): Boolean {
                        callBack(text)
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean = false
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

}