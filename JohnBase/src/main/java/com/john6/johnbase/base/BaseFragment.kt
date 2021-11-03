package com.john6.johnbase.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.john6.johnbase.util.getGenericType

/**
 * On Jetpack navigation
 * Fragments enter/exit represent onCreateView/onDestroyView
 * Thus we should detach all reference to the VIEW on onDestroyView
 */
abstract class BaseFragment<B : ViewBinding> : Fragment() {

    //<editor-fold desc="Google Recommended For ViewBinding in Fragment">
    private var hideBindingForNotLeakMemory:B? = null

    val mBinding: B
    get() = hideBindingForNotLeakMemory!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideBindingForNotLeakMemory = getViewBindingByReflect(inflater, container)
        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideBindingForNotLeakMemory = null
    }
    //</editor-fold>

    fun showLoading(cancelable: Boolean = true) {
        baseActivity.showLoading(cancelable)
    }

    fun dismissLoading() {
        baseActivity.dismissLoading()
    }

    val baseActivity: BaseActivity
        get() = requireActivity() as BaseActivity

    private fun getViewBindingByReflect(inflater: LayoutInflater, container: ViewGroup?) =
        javaClass.getGenericType(0).getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            java.lang.Boolean.TYPE
        ).invoke(null, inflater, container, false) as B

//    /**
//     * Fragments' onConfigurationChanged() is called before Activities
//     * So we just compare current UIMode with BaseActivity's
//     */
//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//        if(newConfig.isNightModeNow != baseActivity.isNightMode)
//            doChangeView()
//    }
//
//    /**
//     * Change your view when UI changes.
//     */
//    abstract fun doChangeView()
}