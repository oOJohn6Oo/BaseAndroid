package com.john6.johnbase.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.john6.johnbase.util.isNightModeNow

abstract class BaseActivity : AppCompatActivity() {
    private var mLoadingDialog: AlertDialog? = null
    var isNightMode = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isNightMode = isNightModeNow
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setupTheme()
    }

    fun showLoading(cancelable: Boolean = true) {
        if (mLoadingDialog == null) {
            mLoadingDialog = AlertDialog.Builder(this).create().apply {
                this.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
                val progressBar = ProgressBar(this.context).apply {
                    isIndeterminate = true
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    )
                }
                setView(progressBar)
            }
        }
        mLoadingDialog?.setCancelable(cancelable)
        mLoadingDialog?.show()
    }

    fun dismissLoading() = mLoadingDialog?.dismiss()

    /**
     * dark mode & system bar
     */
    private fun setupTheme() {
        val wic = WindowInsetsControllerCompat(window, window.decorView)
        wic.isAppearanceLightStatusBars = !isNightMode
        wic.isAppearanceLightNavigationBars = !isNightMode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = Color.TRANSPARENT
        }
    }


/*    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // We detected a UIMode change
        // And we update UI for that
        if (isNightMode != isNightModeNow) {
            isNightMode = !isNightModeNow
            onUIModeChanges()
            setupTheme()
        }
    }*/
//    /**
//     * For not recreate Activity
//     * We must change color manually
//     * But its really dummy
//     */
//    protected abstract fun onUIModeChanges()
}