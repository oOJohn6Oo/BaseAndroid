@file:Suppress("UNUSED_PARAMETER")

package com.john6.appbase

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.google.android.material.color.DynamicColors
import com.john6.appbase.databinding.ActivityMainBinding
import com.john6.johnbase.util.InsetsHelper
import com.john6.johnbase.util.safeDrawing
import com.john6.johnbase.util.tooltips.JDefaultTooltips
import com.john6.johnbase.util.tooltips.TipEdge
import com.john6.johnbase.util.tooltips.showAsPopupAsideView
import com.john6.johnbase.util.vdp
import com.john6.johnbase.util.visible

@SuppressLint("StaticFieldLeak")
lateinit var app: Context

/**
 * Since this APP is One-Activity Multi-Fragment
 * Actually we don't need a Base
 * But just in case.
 */
class MainActivity : FragmentActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private val insetsHelper = InsetsHelper()
    private val onDestChangeListener =
        NavController.OnDestinationChangedListener(this::onDestinationChanged)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = this.applicationContext
        lifecycle.addObserver(insetsHelper)
        DynamicColors.applyToActivityIfAvailable(this)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initView()
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
        mBinding.titleAttMain.setOnClickListener(this::onTitleClicked)
        insetsHelper.onInsetsChanged = { insets ->
            val safeContent = insets.safeDrawing()
            mBinding.toolbarAttMain.setPaddingRelative(
                safeContent.left,
                safeContent.top,
                safeContent.right,
                0
            )
        }
    }

    private fun onTitleClicked(view: View) {
        JDefaultTooltips(view.context).apply {
            val currentId = findNavController(R.id.container_att_main).currentDestination?.id
            mChildBinding.titleItemDt.setText(mapCurrentDstId2TipStrId(currentId))
            insetTop = 5.vdp.toInt()
            insetBottom = 0
            tipEdge = TipEdge.edgeTop
            hideOrShowTailIcon()
            showAsPopupAsideView(
                anchorView = view,
                marginStart = 16.vdp.toInt(),
                marginEnd = 16.vdp.toInt(),
                onTopOfView = false,
            )
        }
    }

    @StringRes
    private fun mapCurrentDstId2TipStrId(destId: Int?): Int {
        return when (destId) {
            R.id.demo1Fragment -> R.string.tooltips_fragment_1
            R.id.demo2LoadingFragment -> R.string.tooltips_fragment_loading
            R.id.demo3ComposeFragment -> R.string.tooltips_fragment_compose
            else -> R.string.tooltips_contact_me
        }
    }

    private fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        mBinding.navBtnAttMain.visible(destination.id != R.id.mainFragment)
        mBinding.titleAttMain.text = destination.label
    }
}