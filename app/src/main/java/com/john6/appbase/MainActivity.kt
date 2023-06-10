package com.john6.appbase

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.john6.appbase.databinding.ActivityMainBinding
import com.john6.johnbase.util.InsetsHelper
import com.john6.johnbase.util.visible

/**
 * Since this APP is One-Activity Multi-Fragment
 * Actually we don't need a Base
 * But just in case.
 */
class MainActivity : FragmentActivity() {
    private lateinit var mBinding:ActivityMainBinding
    private val insetsHelper = InsetsHelper()
    private val onDestChangeListener = NavController.OnDestinationChangedListener(this::onDestinationChanged)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(insetsHelper)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initView()
        insetsHelper.statusBarResponseView = mBinding.toolbarAttMain
    }

    override fun onStart() {
        super.onStart()
        // this is stupid, we CANNOT get navController in onCreate.
        // but add listener in onStart will have more and more listener
        // when activity's lifeCycle Changes in some situation.

        // so be it ？？？？ or we can use liveData
        findNavController(R.id.container_att_main).let {
            it.removeOnDestinationChangedListener(onDestChangeListener)
            it.addOnDestinationChangedListener(onDestChangeListener)
        }
    }

    private fun initView() {
        mBinding.navBtnAttMain.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun onDestinationChanged(controller: NavController ,destination: NavDestination , arguments: Bundle?){
        mBinding.navBtnAttMain.visible(destination.id != R.id.mainFragment)
        mBinding.titleAttMain.text = destination.label
    }
}