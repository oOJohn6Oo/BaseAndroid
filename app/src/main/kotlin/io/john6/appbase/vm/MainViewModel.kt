package io.john6.appbase.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import io.john6.appbase.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var availablePage: MutableLiveData<MutableList<NavDestination>> = MutableLiveData(
        mutableListOf()
    )

    fun getAvailablePage(): LiveData<MutableList<NavDestination>> = availablePage

    fun initNavGraphData(navController: NavController) = CoroutineScope(Dispatchers.IO).launch{
        availablePage.value?.takeIf { it.isEmpty() }?.let {
            navController.graph.forEach { dest ->
                if (dest.id != R.id.mainFragment) {
                    it.add(dest)
                }
            }
            availablePage.postValue(it)
        }
    }

}