package io.john6.base.util.tooltips

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.View.MeasureSpec
import android.view.Window
import android.widget.FrameLayout
import android.widget.PopupWindow
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.Px
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.withStyledAttributes
import androidx.core.view.doOnNextLayout
import com.google.android.material.shape.MaterialShapeUtils
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.Shapeable
import io.john6.johnbase.R
import io.john6.base.util.getRectInWindow
import io.john6.base.util.vdp


/**
 * 便捷设置带三角指示器的CardView
 *
 * cardCornerRadius 优先级高于 shapeAppearance
 *
 * 目前只考虑**一个边**的情况
 *
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
open class JTooltipsLayout : FrameLayout, Shapeable {

    private val jTooltipsHelper: JTooltipsHelper = JTooltipsHelper(this)

    var insetLeft: Int
        get() = jTooltipsHelper.insetLeft
        set(value) {
            jTooltipsHelper.setInsetLeft(value)
        }

    var insetRight: Int
        get() = jTooltipsHelper.insetRight
        set(value) {
            jTooltipsHelper.setInsetRight(value)
        }

    var insetTop: Int
        get() = jTooltipsHelper.insetTop
        set(value) {
            jTooltipsHelper.setInsetTop(value)
        }


    var insetBottom: Int
        get() = jTooltipsHelper.insetBottom
        set(value) {
            jTooltipsHelper.setInsetBottom(value)
        }
    var cornerRadius: Float
        get() = jTooltipsHelper.cornerRadius
        set(value) {
            jTooltipsHelper.setCornerRadius(value)
        }
    var strokeWidth: Int
        get() = jTooltipsHelper.strokeWidth
        set(value) {
            jTooltipsHelper.setStrokeWidth(value)
        }
    var strokeColor: ColorStateList?
        get() = jTooltipsHelper.strokeColor
        set(value) {
            jTooltipsHelper.setStrokeColor(value)
        }

    var rippleColor: ColorStateList?
        get() = jTooltipsHelper.rippleColor
        set(value) {
            jTooltipsHelper.setRippleColor(value)
        }

    @TipDrawStyle
    var tipDrawStyle: Int
        get() = jTooltipsHelper.tipDrawStyle
        set(value) {
            jTooltipsHelper.changeTipDrawStyle(value)
        }

    var tipEdge: Int
        get() = jTooltipsHelper.tipEdge
        set(value) {
            jTooltipsHelper.changeTipEdge(value)
        }

    var tipDrawOffset: Float
        get() = jTooltipsHelper.tipDrawOffset
        set(value) {
            jTooltipsHelper.offsetEdgeTreatment(value)
        }

    @TipDrawGravity
    var tipDrawGravity: Int
        get() = jTooltipsHelper.tipDrawGravity
        set(value) {
            jTooltipsHelper.changeTipDrawGravity(value)
        }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)

    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attr,
        defStyleAttr,
        R.style.defaultJTooltips
    )

    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int, defStyleInt: Int) : super(
        context,
        attr,
        defStyleAttr,
        defStyleInt
    ) {
        context.withStyledAttributes(
            attr,
            R.styleable.JTooltipsLayout,
            defStyleAttr,
            defStyleInt
        ) {
            // Loads and sets background drawable attributes
            jTooltipsHelper.loadFromAttributes(this)
        }

    }

    override fun setShapeAppearanceModel(shapeAppearanceModel: ShapeAppearanceModel) {
        if (isUsingOriginalBackground()) {
            jTooltipsHelper.setShapeAppearanceModel(shapeAppearanceModel)
        } else {
            throw IllegalStateException(
                "Attempted to set ShapeAppearanceModel on a MaterialButton which has an overwritten"
                        + " background."
            )
        }
    }

    override fun getShapeAppearanceModel(): ShapeAppearanceModel {
        return if (isUsingOriginalBackground()) {
            jTooltipsHelper.shapeAppearanceModel
        } else {
            throw java.lang.IllegalStateException(
                "Attempted to get ShapeAppearanceModel from a MaterialButton which has an overwritten"
                        + " background."
            )
        }
    }

    //<editor-fold desc="Background Related">
    override fun setBackgroundTintList(tintList: ColorStateList?) {
        if (isUsingOriginalBackground()) {
            jTooltipsHelper.setBackgroundTintList(tintList)
        } else {
            // If default MaterialButton background has been overwritten, we will let AppCompatButton
            // handle the tinting
            super.setBackgroundTintList(tintList)
        }
    }

    override fun getBackgroundTintList() = jTooltipsHelper.backgroundTint

    override fun setBackgroundTintMode(tintMode: PorterDuff.Mode?) {
        if (isUsingOriginalBackground()) {
            jTooltipsHelper.setBackgroundTintMode(tintMode)
        } else {
            // If default MaterialButton background has been overwritten, we will let AppCompatButton
            // handle the tint Mode
            super.setBackgroundTintMode(tintMode)
        }
    }

    override fun getBackgroundTintMode(): PorterDuff.Mode? {
        return jTooltipsHelper.backgroundTintMode
    }

    override fun setBackgroundColor(@ColorInt color: Int) {
        if (isUsingOriginalBackground()) {
            jTooltipsHelper.setBackgroundColor(color)
        } else {
            super.setBackgroundColor(color)
        }
    }

    override fun setBackground(background: Drawable) {
        setBackgroundDrawable(background)
    }

    /**
     * Update the button's background without changing the background state in [ ]. This should be used when we initially set the background drawable
     * created by [com.google.android.material.button.MaterialButtonHelper].
     *
     * @param background Background to set on this button
     */
    @Suppress("DEPRECATION")
    fun setInternalBackground(background: Drawable?) {
        super.setBackgroundDrawable(background)
    }

    override fun setBackgroundResource(@DrawableRes backgroundResourceId: Int) {
        var background: Drawable? = null
        if (backgroundResourceId != 0) {
            background = AppCompatResources.getDrawable(context, backgroundResourceId)
        }
        background?.also { setBackgroundDrawable(it) }
    }

    @Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
    override fun setBackgroundDrawable(background: Drawable) {
        if (isUsingOriginalBackground()) {
            if (background !== this.background) {
                // this cause insets couldn't be set programmatically
                jTooltipsHelper.setBackgroundOverwritten()
                super.setBackgroundDrawable(background)
            } else {
                // ViewCompat.setBackgroundTintList() and setBackgroundTintMode() call setBackground() on
                // the view in API 21, since background state doesn't automatically update in API 21. We
                // capture this case here, and update our background without replacing it or re-tinting it.
                getBackground().state = background.state
            }
        } else {
            super.setBackgroundDrawable(background)
        }
    }
    //</editor-fold>

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isUsingOriginalBackground()) {
            jTooltipsHelper.getMaterialShapeDrawable()?.also {
                MaterialShapeUtils.setParentAbsoluteElevation(this, it)
            }
        }
    }


    override fun setElevation(elevation: Float) {
        super.setElevation(elevation)
        if (isUsingOriginalBackground()) {
            jTooltipsHelper.getMaterialShapeDrawable()?.elevation = elevation
        }
    }

    fun setPercentCornerRadius(@FloatRange(from = 0.0, to = 1.0) cornerRadius: Float) {
        jTooltipsHelper.setPercentCornerRadius(cornerRadius)
    }

    private fun isUsingOriginalBackground(): Boolean {
        return !jTooltipsHelper.backgroundOverwritten
    }

    /**
     * Sets the ripple color resource for this button.
     *
     * @param rippleColorResourceId Color resource to use for the ripple.
     * @attr ref com.google.android.material.R.styleable#MaterialButton_rippleColor
     * @see #setRippleColor(ColorStateList)
     * @see #getRippleColor()
     */
    open fun setRippleColorResource(@ColorRes rippleColorResourceId: Int) {
        if (isUsingOriginalBackground()) {
            rippleColor = AppCompatResources.getColorStateList(context, rippleColorResourceId)
        }
    }
}

fun JTooltipsLayout.configWhenShowInPopupWindow(popupWindow: PopupWindow){
    popupWindow.contentView = this
//    前面在preparePopup方法中，判断了只有当mBackground不为null，
//    才包装了PopupViewContainer 处理点击 popupWindow外部的时候，会dismiss
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }else {
        popupWindow.setBackgroundDrawable(null)
    }
    popupWindow.isTouchable = true
    popupWindow.isOutsideTouchable = true
    popupWindow.isFocusable = true
    popupWindow.isClippingEnabled = false
}


/**
 * 以 [PopupWindow] 的方式显示 [JTooltipsLayout] 并根据 [anchorView] 自动计算位置及尖角信息
 *
 * @param anchorView 目标View
 * @param onTopOfView 是否在目标View上方显示
 * @param marginStart 距离屏幕左侧的最小距离 注意，
 * @param marginEnd 距离屏幕右侧的最小距离
 * @param contentMargin 内容与边缘的距离
 * @param window 目标Window, 默认取 context 对应的 Activity 的 Window，
 * 这样无法在 Dialog 中使用，所以需要手动传入
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun JTooltipsLayout.showAsPopupAsideView(
    anchorView: View,
    onTopOfView: Boolean = true,
    @Px marginStart: Int = 0,
    @Px marginEnd: Int = 0,
    @Px contentMargin :Int = 0,
    window: Window? = null
) {
    val doShow = {
        val rectView = anchorView.getRectInWindow()
        showAsPopupAsideView(
            rectViewInWindow = rectView,
            onTopOfView = onTopOfView,
            desireMarginStart = marginStart,
            desireMarginEnd = marginEnd,
            contentMargin = contentMargin,
            window = window
        )
    }

    if (anchorView.isLaidOut) {
        doShow()
    } else {
        anchorView.doOnNextLayout {
            doShow()
        }
    }
}

/**
 * 以 [PopupWindow] 的方式显示 [JTooltipsLayout] 并根据 [rectViewInWindow] 自动计算位置及尖角信息
 *
 * @param rectViewInWindow 目标View在Window中的位置
 * @param onTopOfView 是否在目标View上方显示
 * @param desireMarginStart 距离屏幕左侧的最小距离
 * @param desireMarginEnd 距离屏幕右侧的最小距离
 * @param contentMargin 内容与边缘的距离
 * @param window 目标Window, 默认取 context 对应的 Activity 的 Window，
 * 这样无法在 Dialog 中使用，所以需要手动传入
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun JTooltipsLayout.showAsPopupAsideView(
    rectViewInWindow: Rect,
    onTopOfView: Boolean = true,
    @Px desireMarginStart: Int = 0,
    @Px desireMarginEnd: Int = 0,
    @Px contentMargin: Int = 0,
    window: Window? = null
) {
    if (context !is Activity) return
    val desireWindow = window ?: (context as Activity).window ?: return

    insetLeft = desireMarginStart
    insetRight = desireMarginEnd

    @IntRange(from = 0L)
    val minX = desireMarginStart
    val rectCenterPoint = rectViewInWindow.width() / 2f + rectViewInWindow.left

    val windowWidth = desireWindow.decorView.measuredWidth.takeIf { it != 0 }
        ?: resources.configuration.screenWidthDp.vdp.toInt()
    val windowHeight = desireWindow.decorView.measuredHeight.takeIf { it != 0 }
        ?: resources.configuration.screenHeightDp.vdp.toInt()
    // 最大可用宽度直接使用 Window 宽度，因为 insetLeft 和 insetRight 已经作为Padding给到了 View
    val widthMeasureSpec = MeasureSpec.makeMeasureSpec(windowWidth, MeasureSpec.AT_MOST)
    val heightMeasureSpec = MeasureSpec.makeMeasureSpec(windowHeight, MeasureSpec.AT_MOST)
    measure(widthMeasureSpec, heightMeasureSpec)

    val tooltipsContentWidth = measuredWidth - desireMarginStart - desireMarginEnd
    // Layout 的 x 的最大值
    val maxX = windowWidth - desireMarginStart - desireMarginEnd - tooltipsContentWidth

    // Layout 去除 insetLeft, insetRight 后，中点距离 rectCenterPoint 的距离
    val diffX = rectCenterPoint - tooltipsContentWidth / 2f

    val tooltipsOffsetX: Int = when {
        diffX <= minX -> 0
        diffX - desireMarginStart >= maxX -> maxX
        else -> (diffX - desireMarginStart).toInt()
    }

    val tooltipsOffsetY: Int = if (onTopOfView) {
        rectViewInWindow.top - measuredHeight - contentMargin
    } else {
        rectViewInWindow.bottom + contentMargin
    }
    val desireTipsOffset = tooltipsOffsetX + tooltipsContentWidth / 2f + desireMarginStart - rectCenterPoint
    val offsetMultiple = when (tipEdge) {
        TipEdge.edgeTop, TipEdge.edgeStart ->  1f
        else -> -1f
    }

    tipDrawOffset = offsetMultiple * desireTipsOffset

    val popup = PopupWindow(this.context)
    configWhenShowInPopupWindow(popup)
    popup.width = measuredWidth
    popup.height = measuredHeight

    popup.showAtLocation(desireWindow.decorView, Gravity.NO_GRAVITY, tooltipsOffsetX, tooltipsOffsetY)
}

/**
 * 附着到View上，并自动计算尖角的位置
 * 注意：这需要 View 被添加到布局中
 *
 * @param anchorView 附着的View
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun JTooltipsLayout.applyTipDrawOffsetByAnchorView(anchorView: View, sameParent: Boolean = true) {
    // 统一从中间开始偏移
    this.tipDrawGravity = TipDrawGravity.drawGravityCenter
    this.tipDrawOffset = 0f

    var tooltipsCenter = -1f
    var viewCenter = -1f

    val isHorizontal = when (tipEdge) {
        TipEdge.edgeHorizontal, TipEdge.edgeTop, TipEdge.edgeBottom -> true
        else -> false
    }
    val offsetMultiple = when (tipEdge) {
        TipEdge.edgeTop, TipEdge.edgeStart -> 1f
        else -> -1f
    }

    this.addOnLayoutChangeListener { v, left, top, right, bottom, _, _, _, _ ->
        tooltipsCenter = if(sameParent){
            ((left + right) / 2f).takeIf { isHorizontal } ?: ((top + bottom) / 2f)
        }else{
            v.getRectInWindow().centerX().toFloat().takeIf { isHorizontal } ?: v.getRectInWindow()
                .centerY().toFloat()
        }
        if (viewCenter != -1f) {
            (v as JTooltipsLayout).tipDrawOffset = offsetMultiple * (tooltipsCenter - viewCenter)
        }
    }
    anchorView.addOnLayoutChangeListener { v, left, top, right, bottom, _, _, _, _ ->
        viewCenter = if (sameParent){
            ((left + right) / 2f).takeIf { isHorizontal } ?: ((top + bottom) / 2f)
        }else{
            v.getRectInWindow().let {
                it.centerX().takeIf { isHorizontal } ?: it.centerY()
            }.toFloat()
        }
        if (tooltipsCenter != -1f) {
            this.tipDrawOffset = offsetMultiple * (tooltipsCenter - viewCenter)
        }
    }
}