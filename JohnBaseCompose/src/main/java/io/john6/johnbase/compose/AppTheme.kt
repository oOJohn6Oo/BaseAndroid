package io.john6.johnbase.compose

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.Window
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

@Composable
fun JohnAppTheme(content: @Composable () -> Unit) {
    val isDarkTheme = isSystemInDarkTheme()
    val view = LocalView.current
    LaunchedEffect(isDarkTheme){
        // change status bar color and nav bar color
        view.context.findWindow()?.apply {
            WindowCompat.getInsetsController(this, view).apply {
                isAppearanceLightStatusBars = !isDarkTheme
                isAppearanceLightNavigationBars = !isDarkTheme
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

fun Context.findWindow(): Window? {
    return when (this) {
        is Activity -> window
        is ContextWrapper -> baseContext.findWindow()
        else -> null
    }
}