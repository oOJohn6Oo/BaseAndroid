package com.john6.appbase

import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.HapticFeedbackConstants.VIRTUAL_KEY
import android.view.View
import android.view.Window
import android.view.animation.OvershootInterpolator
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import java.lang.reflect.ParameterizedType

//<editor-fold desc="Must Have">
fun String.log(debug: Boolean = true) {
    if (debug)
        Log.d("lq", this)
    else
        Log.e("lq", this)
}

val Int.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

val Float.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

val Int.sp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

val Float.sp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        Resources.getSystem().displayMetrics
    )
//</editor-fold>


//<editor-fold desc="Tool">
fun Window.hideKeyboard(requestView: View = this.decorView) {
    WindowCompat.getInsetsController(this, requestView)?.hide(WindowInsetsCompat.Type.ime())
}

fun Window.showKeyboard(requestView: View = this.decorView) {
    WindowCompat.getInsetsController(this, requestView)?.show(WindowInsetsCompat.Type.ime())
}

fun Class<*>.getGenericType(index:Int) =
    (this.genericSuperclass as ParameterizedType).actualTypeArguments[index] as Class<*>
//</editor-fold>

//<editor-fold desc="Theme & UI related">
val isNightModeNow: Boolean
    get() = Resources.getSystem().configuration.isNightModeNow

val Configuration.isNightModeNow
    get() = this.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES


/**
 * Get the id you want
 * eg: {@link android.R.attr.actionBarSize}
 */
fun Context.getAttrResId(@AttrRes attr: Int) =
    TypedValue().let {
        this.theme.resolveAttribute(attr, it, true)
        it.resourceId
    }

/**
 * I found some color attrs do not have Id
 * So {@link this#getAttrResId} do not work
 */
fun Context.getColorInt(@AttrRes colorAttrId: Int) =
    TypedValue().let { tv ->
        this.theme.resolveAttribute(colorAttrId, tv, true)
        ContextCompat.getColor(this, tv.resourceId).takeIf { tv.type == TypedValue.TYPE_STRING }
            ?: tv.data
    }

val Int.tint
    get() = ColorStateList.valueOf(this)


//</editor-fold>

//<editor-fold desc="Animation Stuff">

fun View.shake(duration: Long = 500L, p: Float) {
    performHapticFeedback(VIRTUAL_KEY)
    postDelayed({ this.performHapticFeedback(VIRTUAL_KEY) }, duration / 5)
    postDelayed({ this.performHapticFeedback(VIRTUAL_KEY) }, duration * 3 / 5)
    ObjectAnimator.ofFloat(this, View.TRANSLATION_X, 0f, p, -p, 0f, p, 0f).apply {
        interpolator = OvershootInterpolator()
        this.duration = duration
        start()
    }
}

operator fun View.times(endView: View) = MaterialContainerTransform().let {
    it.startView = this
    it.endView = endView
    it.addTarget(endView)
    it.setPathMotion(MaterialArcMotion())
    it.duration = 500
    it.scrimColor = Color.TRANSPARENT
}
//</editor-fold>