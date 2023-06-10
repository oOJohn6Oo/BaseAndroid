package com.john6.johnbase.util

import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.Resources
import android.util.Log
import android.util.TypedValue
import android.view.HapticFeedbackConstants.VIRTUAL_KEY
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.view.animation.OvershootInterpolator
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.DefaultLifecycleObserver
import java.lang.reflect.ParameterizedType

//<editor-fold desc="Must Have">
fun String.log(debug: Boolean = true) {
    if (debug)
        Log.d("lq", this)
    else
        Log.e("lq", this)
}

val Int.vdp
    get() = this.toFloat().vdp

val Float.vdp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

val Int.sp
    get() = this.toFloat().vsp

val Float.vsp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        Resources.getSystem().displayMetrics
    )


class ViewBindingObserver:DefaultLifecycleObserver{
}

//</editor-fold>


//<editor-fold desc="Tool">
fun Window.hideKeyboard(requestView: View = this.decorView) {
    WindowCompat.getInsetsController(this, requestView)?.hide(WindowInsetsCompat.Type.ime())
}

fun Window.showKeyboard(requestView: View = this.decorView) {
    WindowCompat.getInsetsController(this, requestView)?.show(WindowInsetsCompat.Type.ime())
}

fun Class<*>.getGenericType(index: Int) =
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

fun View.visible(show: Boolean = true) {
    this.visibility = if (show) VISIBLE else GONE
}
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
//</editor-fold>

fun View.setBottomPadding(desirePadding: Int) {
    this.setPadding(
        this.paddingLeft,
        this.paddingTop,
        this.paddingRight,
        desirePadding
    )
}

fun View.setTopPadding(desirePadding: Int) {
    this.setPadding(
        this.paddingLeft,
        desirePadding,
        this.paddingRight,
        this.paddingBottom
    )
}

fun View.setBottomMargin(desireMargin: Int) {
    val lp = this.layoutParams as? ViewGroup.MarginLayoutParams ?: return
    lp.bottomMargin = desireMargin
    this.layoutParams = lp
}

fun View.setTopMargin(desireMargin: Int) {
    val lp = this.layoutParams as? ViewGroup.MarginLayoutParams ?: return
    lp.topMargin = desireMargin
    this.layoutParams = lp
}