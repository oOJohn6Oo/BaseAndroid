@file:Suppress("MemberVisibilityCanBePrivate")

package com.john6.johnbase.util

import android.app.Activity
import android.content.ComponentCallbacks
import android.content.ContentResolver
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner


/**
 * 全面屏适配工具类
 *
 * | Edge-To-Edge | 系统遮罩 | 手势判断 | 生命周期感知 |
 * ``` kotlin
 * class MyActivity: AppCompatActivity() {
 *   private val insetsHelper = InsetsHelper()
 *   override fun onCreate(savedInstanceState: Bundle?) {
 *     super.onCreate(savedInstanceState)
 *     lifecycle.addObserver(insetsHelper)
 *     // init ViewBinding
 *     // Config Inset View
 *     insetsHelper.statusBarResponseView = binding.toolBarAttMain
 *     insetsHelper.navBarResponseView = binding.recycleViewAttMain
 *   }
 * }
 * ```
 */
open class InsetsHelper : DefaultLifecycleObserver {

    /**
     * 如果拦截 WindowInsets，则会强制分发一次 WindowInsets 到这个View
     */
    var dispatchView: View? = null
    var consumeWindowInsets = false

    // 响应状态栏的View
    var statusBarResponseView: View? = null

    // 状态栏响应模式，默认 true => padding, false => margin
    var statusBarRespondPadding: Boolean = true

    // 状态栏改变时会调用此方法
    var onStatusBarInsetChanged: ((height: Int) -> Unit)? = null

    // 响应导航栏的View
    var navBarResponseView: View? = null

    // 导航栏响应模式，默认 true => padding, false => margin
    var navBarRespondPadding: Boolean = true

    // 导航栏改变时会调用此方法
    var onNavBarInsetChanged: ((height: Int) -> Unit)? = null

    /**
     * 是否启用状态栏遮罩
     * * true 启用
     * * false 不启用
     * * null 不管
     */
    var enforceStatusBar: Boolean? = false

    /**
     * 是否启用导航栏遮罩
     * * true 启用
     * * false 不启用
     * * null 不管
     */
    var enforceNavBar: Boolean? = false

    private var window: Window? = null
    private var observeView: View? = null

    /**
     * 忽略所有情况，强制导航栏为此颜色
     */
    var forceNavBarColor: Int? = null


    // 遮罩颜色
    open val scrimColor: Int
        get() = if (isInNightMode(
                observeView?.resources?.configuration ?: Resources.getSystem().configuration
            )
        ) blackScrimColor else whiteScrimColor


    /**
     * 发现在启动新 Activity 时，最后会回调一次 旧 Activity 的 setOnApplyWindowInsetsListener
     * 所以设置在[onStop]时手动设置次标志位为 false，避免影响新 Activity 的行为
     *
     * 2023-03-06 更新 因为只影响页面间导航栏颜色，所以移动逻辑到[configNavBarColor]外
     */
    private var enableResponse = true

    override fun onCreate(owner: LifecycleOwner) {
        ensureWindow(owner)
        ensureObserveView(owner)
        listenToConfigChange(owner)

        window?.also {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                it.isStatusBarContrastEnforced = false
                it.isNavigationBarContrastEnforced = false
            }
            WindowCompat.setDecorFitsSystemWindows(it, false)
            // 谷歌的一个BUG，安卓8.0不支持XML设置，只能代码设置
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
                WindowCompat.getInsetsController(it, it.decorView).isAppearanceLightNavigationBars =
                    true
            }
        }

        val desiredView = observeView ?: return

        ViewCompat.setOnApplyWindowInsetsListener(desiredView) { _, insets ->

            val inset = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            configStatusBarResponse(inset.top)
            configNavBarResponse(inset.bottom)
            if (enableResponse && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                configNavBarColor(insets)
            }
            if (consumeWindowInsets) {
                dispatchView?.also {
                    ViewCompat.dispatchApplyWindowInsets(it, insets)
                }
                WindowInsetsCompat.CONSUMED
            } else {
                insets
            }
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        enableResponse = true
        super.onStart(owner)
        ensureWindow(owner)
        ensureObserveView(owner)
        if (observeView == null) return
        if (owner is Fragment) {
            // we can touch viewLifecycleOwner only if view is not null
            // 内部使用 Map 存储，多次 add 不会有问题
            owner.viewLifecycleOwner.lifecycle.addObserver(this@InsetsHelper)
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        enableResponse = false
        super.onStop(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        dispatchView = null
        statusBarResponseView = null
        navBarResponseView = null
        observeView = null
        window = null
    }


    private fun listenToConfigChange(lifecycleOwner: LifecycleOwner) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return
        val activity = when (lifecycleOwner) {
            is Activity -> {
                lifecycleOwner
            }

            is Fragment -> {
                lifecycleOwner.requireActivity()
            }

            else -> {
                null
            }
        } ?: return

        doListenToDarkModeChange(activity)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun doListenToDarkModeChange(activity: Activity) {
        activity.registerComponentCallbacks(object : ComponentCallbacks {
            override fun onLowMemory() {
                // do nothing
            }

            override fun onConfigurationChanged(newConfig: Configuration) {
                val view = observeView ?: return

                val isOldInNightMode = isInNightMode(view.resources.configuration)
                val isCurrentInNightMode = isInNightMode(newConfig)

                if (isCurrentInNightMode != isOldInNightMode) {
                    view.dispatchApplyWindowInsets(view.rootWindowInsets)
                }

            }
        })
    }

    /**
     * 设置状态栏响应相关
     *
     * @param height 状态栏高度
     */
    private fun configStatusBarResponse(height: Int) {
        statusBarResponseView?.also {
            if (statusBarRespondPadding) {
                it.setTopPadding(height)
            } else {
                it.setTopMargin(height)
            }
        }
        onStatusBarInsetChanged?.invoke(height)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (enforceStatusBar == true) {
                window?.statusBarColor = scrimColor
            } else if (enforceStatusBar == false) {
                window?.statusBarColor = Color.TRANSPARENT
            }
        }
    }

    /**
     * 设置导航栏响应相关
     *
     * @param height 导航栏高度
     */
    private fun configNavBarResponse(height: Int) {
        navBarResponseView?.also {
            if (navBarRespondPadding) {
                it.setBottomPadding(height)
            } else {
                it.setBottomMargin(height)
            }
        }
        onNavBarInsetChanged?.invoke(height)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun configNavBarColor(insets: WindowInsetsCompat) {
        val desireEnforceNavBar = enforceNavBar ?: return

        val contentResolver = observeView?.context?.contentResolver
        var desireNavColor: Int =
            if (desireEnforceNavBar && !isGestureNav(contentResolver, insets)) {
                scrimColor
            } else {
                Color.TRANSPARENT
            }

        forceNavBarColor?.also {
            desireNavColor = it
        }

        window?.navigationBarColor = desireNavColor
    }

    /**
     * 直接设置导航栏颜色
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun directlyChangeNavBarColor(navBarColor: Int) {
        window?.navigationBarColor = navBarColor
    }

    private fun findContentView(): View? {
        return window?.decorView?.findViewById(android.R.id.content)
    }

    /**
     * 根据 source 寻找 window
     *
     * @return window 没有则返回 null
     */
    private fun ensureWindow(source: LifecycleOwner) {
        window = when (source) {
            is DialogFragment -> {
                source.dialog?.window
            }

            is Fragment -> {
                source.requireActivity().window
            }

            is Activity -> {
                source.window
            }

            else -> window
        }
    }

    private fun ensureObserveView(source: LifecycleOwner) {
        when (source) {
            is DialogFragment, is Activity -> {
                observeView = findContentView()
            }

            is Fragment -> {
                observeView = source.view
            }
        }
    }

    companion object {
        val systemWhiteScrimColor by lazy {
            Color.parseColor("#E6FFFFFF") // 90% white
        }
        val systemBlackScrimColor by lazy {
            Color.parseColor("#66000000") // 40% black
        } // 40% black

        val whiteScrimColor by lazy {
            Color.parseColor("#99FFFFFF") // 60% white
        }
        val blackScrimColor by lazy {
            Color.parseColor("#4D000000") // 30% black
        }

        /**
         * 根据[androidx.core.graphics.Insets.left]的具体值判断是否为手势导航
         * 针对MIUI特殊判断
         *
         * @param insets 当前 Window 的相关信息
         * @return 手势导航 ==> true, 按钮导航 ==> false
         */
        fun isGestureNav(contentResolver: ContentResolver?, insets: WindowInsetsCompat): Boolean {
            if (isMiUI) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) return false
                val miNavBarMode =
                    Settings.Global.getInt(contentResolver, "force_fsg_nav_bar", 0)
                return miNavBarMode == 1
            }
            val inset = insets.getInsets(WindowInsetsCompat.Type.systemGestures())
            return inset.left > 0
        }

        val isMiUI by lazy {
            getSystemProperty("ro.miui.ui.version.name").isNotBlank()
        }

        fun getSystemProperty(propName: String) = try {
            val systemProperties = Class.forName("android.os.SystemProperties")
            val method = systemProperties.getMethod("get", String::class.java)
            method.invoke(systemProperties, propName) as String
        } catch (e: Exception) {
            ""
        }

        private fun isInNightMode(configuration: Configuration): Boolean {
            return configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        }
    }

}