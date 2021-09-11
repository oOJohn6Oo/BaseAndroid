package com.john6.appbase.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.john6.appbase.R

class MainViewModel() : ViewModel() {
    private var availablePage: MutableLiveData<MutableList<NavDestination>> = MutableLiveData(
        mutableListOf()
    )

    fun getAvailablePage(): LiveData<MutableList<NavDestination>> = availablePage

    fun initNavGraphData(navController: NavController) {
        availablePage.value?.let {
            navController.graph.forEach { dest ->
                if (dest.id != R.id.mainFragment)
                    it.add(dest)
            }
            availablePage.postValue(it)
        }
    }

}