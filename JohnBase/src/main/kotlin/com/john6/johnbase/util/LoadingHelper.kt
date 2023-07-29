@file:Suppress("DEPRECATION")

package com.john6.johnbase.util


import android.app.ProgressDialog
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * Loading 弹窗 工具类
 *
 * 只需 `ProgressHelper.show(context)` 或 `ProgressHelper.dismiss(context)` 即可显示隐藏 LoadingDialog
 *
 * * 支持延时启动
 * * 多个 Fragment 只显示一个 LoadingDialog
 * * 自动处理生命周期
 */
object LoadingHelper : DefaultLifecycleObserver {
    private val mDialogList = mutableMapOf<LifecycleOwner, ProgressDialog?>()
    private val mDialogShowingMap = mutableMapOf<LifecycleOwner, Boolean>()

    /**
     * @param showNow false -> show after 500L; true -> show now
     */
    @MainThread
    fun show(
        context: Context?,
        @StringRes msgResId: Int = android.R.string.ok,
        showNow: Boolean = false
    ) {
        if (context is ComponentActivity) {
            if (context.isFinishing) return
            mDialogShowingMap[context] = true
            val dialog: ProgressDialog? = mDialogList[context]
            if (dialog == null) {
                context.lifecycle.addObserver(this)
                ProgressDialog(context).also {
                    it.setMessage(context.getString(msgResId))
                    it.setCancelable(false)
                    it.setCanceledOnTouchOutside(false)
                    mDialogList[context] = it
                    checkWhetherNeedShowDialog(showNow, context, it)
                }
            } else {
                checkWhetherNeedShowDialog(showNow, context, dialog)
            }
        }
    }

    @MainThread
    fun dismiss(context: Context?) {
        if (context is ComponentActivity && mDialogList.containsKey(context)) {
            mDialogList[context]?.dismiss()
            mDialogShowingMap[context] = false
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        val dialog = mDialogList[owner]
        dialog?.dismiss()
        mDialogList[owner] = null
        mDialogList.remove(owner)
        mDialogShowingMap.remove(owner)
    }

    private fun checkWhetherNeedShowDialog(
        showNow: Boolean,
        activity: ComponentActivity,
        dialog: ProgressDialog
    ) {
        if (showNow) {
            dialog.show()
        } else {
            activity.window.decorView.postDelayed({
                if (mDialogShowingMap[activity] == true) {
                    dialog.show()
                }
            }, 500L)
        }
    }
}