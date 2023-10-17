@file:Suppress("UNUSED_PARAMETER")

package io.john6.appbase

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.google.android.material.color.DynamicColors
import io.john6.appbase.databinding.ActivityMainBinding
import io.john6.base.util.JInsetsHelper
import io.john6.base.util.safeDrawing
import io.john6.base.util.tooltips.JDefaultTooltips
import io.john6.base.util.tooltips.TipEdge
import io.john6.base.util.tooltips.showAsPopupAsideView
import io.john6.base.util.vdp
import io.john6.base.util.visible

@SuppressLint("StaticFieldLeak")
lateinit var app: Context

/**
 * Since this APP is One-Activity Multi-Fragment
 * Actually we don't need a Base
 * But just in case.
 */
class MainActivity : FragmentActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private val JInsetsHelper = JInsetsHelper()
    private val onDestChangeListener =
        NavController.OnDestinationChangedListener(this::onDestinationChanged)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = this.applicationContext
        lifecycle.addObserver(JInsetsHelper)
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
        mBinding.titleAttMain.setOnLongClickListener(this::onTitleLongClicked)
        JInsetsHelper.onInsetsChanged = { insets ->
            val safeContent = insets.safeDrawing()
            mBinding.toolbarAttMain.setPaddingRelative(
                safeContent.left,
                safeContent.top,
                safeContent.right,
                0
            )
        }
    }

    private fun onTitleLongClicked(view: View?): Boolean {
        if (view !is TextView) return false
        val presetIndex =
            when ((view.layoutParams as FrameLayout.LayoutParams).gravity) {
                Gravity.START -> 0
                Gravity.CENTER -> 1
                else -> 2
            }
        AlertDialog.Builder(this)
            .setSingleChoiceItems(
                arrayOf("Start", "Center", "End"),
                presetIndex
            ) { p0, selectIndex ->
                p0.dismiss()
                val lp = mBinding.titleAttMain.layoutParams as FrameLayout.LayoutParams
                lp.gravity = when (selectIndex) {
                    0 -> Gravity.START
                    1 -> Gravity.CENTER
                    else -> Gravity.END
                }
                mBinding.titleAttMain.layoutParams = lp
            }
            .show()
        return true
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
                marginEnd = 32.vdp.toInt(),
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