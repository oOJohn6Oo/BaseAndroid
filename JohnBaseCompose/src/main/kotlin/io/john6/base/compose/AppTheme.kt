package io.john6.base.compose

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.ContextWrapper
import android.provider.Settings
import android.view.Window
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

@Composable
fun JAppTheme(
    enableDefaultAppearance: Boolean = true,
    enableDefaultNavScrim: Boolean = true,
    content: @Composable () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    val view = LocalView.current
    if (enableDefaultAppearance){
        LaunchedEffect(isDarkTheme) {
            // change status bar color and nav bar color
            view.context.findWindow()?.apply {
                WindowCompat.getInsetsController(this, view).apply {
                    isAppearanceLightStatusBars = !isDarkTheme
                    isAppearanceLightNavigationBars = !isDarkTheme
                }
            }
        }
    }
    if (enableDefaultNavScrim){
        LaunchedEffect(LocalConfiguration.current){
            view.context.findWindow()?.apply {
                val scrimColor = if (isGestureNav(view.context.contentResolver, ViewCompat.getRootWindowInsets(view))) {
                    Color.Transparent
                } else{
                    if (isDarkTheme) {
                        Color.Black.copy(alpha = 0.3f)
                    } else {
                        Color.White.copy(alpha = 0.6f)
                    }
                }
                navigationBarColor = scrimColor.toArgb()
            }
        }
    }
    MaterialTheme(
        colors = if (isDarkTheme) darkColors() else lightColors(),
        shapes = Shapes(
            small = RoundedCornerShape(4.dp),
            medium = RoundedCornerShape(8.dp),
            large = RoundedCornerShape(16.dp)
        ),
        content = content
    )
}

val MaterialTheme.spaceSmall:Dp
    get() = 4.dp

val MaterialTheme.spaceMedium:Dp
    get() = 8.dp

val MaterialTheme.spaceLarge:Dp
    get() = 16.dp

@Suppress("PrivateApi")
private val isMiUI by lazy {
    val version = try {
        val systemProperties = Class.forName("android.os.SystemProperties")
        val method = systemProperties.getMethod("get", String::class.java)
        method.invoke(systemProperties, "ro.miui.ui.version.name") as String
    } catch (e: Exception) {
        ""
    }
    version.isNotBlank()
}

/**
 * 根据[androidx.core.graphics.Insets]的具体值判断是否为手势导航
 * 针对MIUI特殊判断
 *
 * @return 手势导航 ==> true, 按钮导航 ==> false
 */
private fun isGestureNav(contentResolver: ContentResolver?, insets: WindowInsetsCompat?): Boolean {
    if (isMiUI) {
        val miNavBarMode =
            Settings.Global.getInt(contentResolver, "force_fsg_nav_bar", 0)
        return miNavBarMode == 1
    }
    if(insets == null) return false
    val navbarInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
    val gestureInsets = insets.getInsets(WindowInsetsCompat.Type.systemGestures())
    return if (navbarInsets.right == gestureInsets.right) false
    else gestureInsets.left > 0
}

fun Context.findWindow(): Window? {
    return when (this) {
        is Activity -> window
        is ContextWrapper -> baseContext.findWindow()
        else -> null
    }
}