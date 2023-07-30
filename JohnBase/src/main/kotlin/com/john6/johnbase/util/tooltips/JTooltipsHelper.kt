package com.john6.johnbase.util.tooltips

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.RippleDrawable
import android.util.TypedValue
import android.view.View
import android.view.View.OnLayoutChangeListener
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import com.google.android.material.color.MaterialColors
import com.google.android.material.shape.*
import com.john6.johnbase.R
import com.john6.johnbase.util.tooltips.TipDrawGravity.Companion.drawGravityCenter
import com.john6.johnbase.util.tooltips.TipDrawGravity.Companion.drawGravityEnd
import com.john6.johnbase.util.tooltips.TipDrawGravity.Companion.drawGravityStart
import com.john6.johnbase.util.tooltips.TipDrawStyle.Companion.styleCircle
import com.john6.johnbase.util.tooltips.TipDrawStyle.Companion.styleDefault
import com.john6.johnbase.util.tooltips.TipDrawStyle.Companion.styleRoundTriangle
import com.john6.johnbase.util.tooltips.TipDrawStyle.Companion.styleTriangle
import com.john6.johnbase.util.tooltips.TipEdge.Companion.edgeAll
import com.john6.johnbase.util.tooltips.TipEdge.Companion.edgeBottom
import com.john6.johnbase.util.tooltips.TipEdge.Companion.edgeEnd
import com.john6.johnbase.util.tooltips.TipEdge.Companion.edgeHorizontal
import com.john6.johnbase.util.tooltips.TipEdge.Companion.edgeStart
import com.john6.johnbase.util.tooltips.TipEdge.Companion.edgeTop
import com.john6.johnbase.util.tooltips.TipEdge.Companion.edgeVertical
import com.john6.johnbase.util.vdp


/**
 * [JTooltipsLayout] 的工具类
 *
 */
@RequiresApi(21)
class JTooltipsHelper(
    private val jTooltipsLayout: JTooltipsLayout
) : OnLayoutChangeListener {
    var shapeAppearanceModel = ShapeAppearanceModel().withCornerSize(4.vdp)
    private set

    var tipEdge: Int = edgeBottom
        private set
    var tipDrawGravity = drawGravityCenter
        private set
    var tipDrawOffset = Float.MAX_VALUE
        private set
    var tipDrawInside = false
        private set
    var tipDepth = 10f
        private set
    var tipWidth = 20f
        private set
    var tipDrawStyle = styleDefault
        private set


    var insetLeft = 0
        private set
    var insetRight = 0
        private set
    var insetTop = 0
        private set
    var insetBottom = 0
        private set
    var cornerRadius = 4.vdp
        private set
    var strokeWidth = 0
        private set
    var strokeColor: ColorStateList? = null
        private set
    var backgroundTintMode: PorterDuff.Mode? = null
        private set
    var backgroundTint: ColorStateList? = null
        private set
    var rippleColor: ColorStateList? = null
        private set
    var backgroundOverwritten = false
        private set


    private var mEdgeTreatment: EdgeTreatment = EdgeTreatment()
    private var mOffsetEdgeTreatment: EdgeTreatment? = null

    private var maskDrawable: Drawable? = null
    private val shouldDrawSurfaceColorStroke = false
    private var rippleDrawable: LayerDrawable? = null
    private var elevation = 0
    private var mCornerRadiusType: Int = TypedValue.TYPE_NULL

    fun loadFromAttributes(attributes: TypedArray) {

        tipDrawInside = attributes.getBoolean(R.styleable.JTooltipsLayout_tipDrawInside, false)
        tipEdge = attributes.getInt(R.styleable.JTooltipsLayout_tipEdge, edgeBottom)
        tipDepth = attributes.getDimension(R.styleable.JTooltipsLayout_tipDepth, 0f)
        tipWidth = attributes.getDimension(R.styleable.JTooltipsLayout_tipWidth, 0f)
        tipDrawGravity = attributes.getInt(R.styleable.JTooltipsLayout_tipDrawGravity, drawGravityCenter)
        tipDrawOffset = attributes.getDimension(R.styleable.JTooltipsLayout_tipDrawOffset, 0f)
        val tempTipDrawStyle = attributes.getInt(R.styleable.JTooltipsLayout_tipDrawStyle, styleDefault)

        changeTipDrawStyle(tempTipDrawStyle)

        val valueCornerRadius =
            attributes.peekValue(R.styleable.JTooltipsLayout_shapeCornerRadius)
        if (valueCornerRadius != null) {
            var tempCornerType = TypedValue.TYPE_DIMENSION

            if (valueCornerRadius.type == TypedValue.TYPE_DIMENSION) {
                val tempCornerRadius = TypedValue.complexToDimension(
                    valueCornerRadius.data,
                    jTooltipsLayout.context.resources.displayMetrics
                )
                setCornerRadius(tempCornerRadius)
            } else if (valueCornerRadius.type == TypedValue.TYPE_FRACTION) {
                tempCornerType = TypedValue.TYPE_FRACTION
                cornerRadius = valueCornerRadius.getFraction(1f, 1f)
            }
            mCornerRadiusType = tempCornerType
        }

        if (mCornerRadiusType == TypedValue.TYPE_FRACTION) {
            jTooltipsLayout.addOnLayoutChangeListener(this)
        }


        insetLeft =
            attributes.getDimensionPixelOffset(R.styleable.JTooltipsLayout_android_insetLeft, 0)
        insetTop =
            attributes.getDimensionPixelOffset(R.styleable.JTooltipsLayout_android_insetTop, 0)
        insetRight =
            attributes.getDimensionPixelOffset(R.styleable.JTooltipsLayout_android_insetRight, 0)
        insetBottom =
            attributes.getDimensionPixelOffset(
                R.styleable.JTooltipsLayout_android_insetBottom,
                0
            )

        strokeWidth =
            attributes.getDimensionPixelSize(R.styleable.JTooltipsLayout_strokeWidth, 0)
        backgroundTintMode = parseTintMode(
            attributes.getInt(R.styleable.JTooltipsLayout_android_backgroundTintMode, -1),
            PorterDuff.Mode.SRC_IN
        )
        backgroundTint = getColorStateList(
            jTooltipsLayout.context,
            attributes,
            R.styleable.JTooltipsLayout_android_backgroundTint
        )
        strokeColor = getColorStateList(
            jTooltipsLayout.context,
            attributes,
            R.styleable.JTooltipsLayout_android_strokeColor
        )
        rippleColor = getColorStateList(
            jTooltipsLayout.context, attributes, R.styleable.JTooltipsLayout_rippleColor
        )
        elevation =
            attributes.getDimensionPixelSize(R.styleable.JTooltipsLayout_android_elevation, 0)

        // Store padding before setting background, since background overwrites padding values
        val paddingStart = ViewCompat.getPaddingStart(jTooltipsLayout)
        val paddingTop: Int = jTooltipsLayout.paddingTop
        val paddingEnd = ViewCompat.getPaddingEnd(jTooltipsLayout)
        val paddingBottom: Int = jTooltipsLayout.paddingBottom

        // Update ceDefaultTooltips2's background without triggering setBackgroundOverwritten()
        if (attributes.hasValue(R.styleable.JTooltipsLayout_android_background)) {
            setBackgroundOverwritten()
        } else {
            updateBackground()
        }
        // Set the stored padding values
        ViewCompat.setPaddingRelative(
            jTooltipsLayout,
            paddingStart + insetLeft,
            paddingTop + insetTop,
            paddingEnd + insetRight,
            paddingBottom + insetBottom
        )
    }


    /**
     * 设置绘制时的偏移量
     */
    fun offsetEdgeTreatment(@Px offset: Float) {
        tipDrawOffset = offset
        forceUpdateShape()
    }

    fun changeTipEdge(@TipEdge newTipEdge:Int){
        this.tipEdge = newTipEdge
        forceUpdateShape()
    }

    fun changeTipDrawGravity(@TipDrawGravity newTipDrawGravity: Int){
        this.tipDrawGravity = newTipDrawGravity
        forceUpdateShape()
    }

    fun changeTipDrawStyle(@TipDrawStyle newTipDrawStyle:Int){
        this.tipDrawStyle = newTipDrawStyle
        mEdgeTreatment = when (tipDrawStyle) {
            styleTriangle -> TriangleEdgeTreatment(tipDepth, tipDrawInside)
            styleRoundTriangle -> BezierTriangleEdgeTreatment(
                tipWidth,
                tipDepth,
                tipDrawInside,
                tipDrawGravity
            )
            styleCircle -> CircleEdgeTreatment(
                tipDepth,
                tipDrawInside,
                tipDrawGravity
            )
            else -> EdgeTreatment()
        }
        if (tipDrawOffset != Float.MAX_VALUE) {
            mOffsetEdgeTreatment = OffsetEdgeTreatment(mEdgeTreatment, tipDrawOffset)
        }
        onEdgeChanged()
    }


    private fun onEdgeChanged() {
        val edgeTreatment = mOffsetEdgeTreatment.takeIf { it != null } ?: mEdgeTreatment

        configShapeModel {
            // ResetAllEdge
            it.setAllEdges(EdgeTreatment())

            if (tipEdge == edgeStart || tipEdge == edgeHorizontal) {
                it.setLeftEdge(edgeTreatment)
            }

            if (tipEdge == edgeTop) {
                it.setTopEdge(edgeTreatment)
            }

            if (tipEdge == edgeEnd || tipEdge == edgeHorizontal) {
                it.setRightEdge(edgeTreatment)
            }

            if (tipEdge == edgeBottom) {
                it.setBottomEdge(edgeTreatment)
            }

            if (tipEdge == edgeAll) {
                it.setAllEdges(edgeTreatment)
            }
        }
    }

    private fun forceUpdateShape(){
        changeTipDrawStyle(this.tipDrawStyle)
    }

    private inline fun configShapeModel(operation: (ShapeAppearanceModel.Builder) -> Unit) {
        val newModel = shapeAppearanceModel.toBuilder().apply {
            operation(this)
        }.build()
        setShapeAppearanceModel(newModel)
    }

    /**
     * Method that is triggered when our initial background, created by [.createBackground],
     * has been overwritten with a new background. Sets the [.backgroundOverwritten] flag, which
     * disables some of the functionality tied to our custom background.
     */
    fun setBackgroundOverwritten() {
        backgroundOverwritten = true
        jTooltipsLayout.backgroundTintList = backgroundTint
        jTooltipsLayout.backgroundTintMode = backgroundTintMode
    }

    fun setShapeAppearanceModel(shapeAppearanceModel: ShapeAppearanceModel) {
        this.shapeAppearanceModel = shapeAppearanceModel
        updateButtonShape(shapeAppearanceModel)
    }

    private fun updateButtonShape(shapeAppearanceModel: ShapeAppearanceModel) {
        getMaterialShapeDrawable()?.shapeAppearanceModel = shapeAppearanceModel
        getSurfaceColorStrokeDrawable()?.shapeAppearanceModel = shapeAppearanceModel
        getMaskDrawable()?.shapeAppearanceModel = shapeAppearanceModel
    }

    private fun updateBackground() {
        jTooltipsLayout.setInternalBackground(createBackground())
        val materialShapeDrawable = getMaterialShapeDrawable()
        if (materialShapeDrawable != null) {
            materialShapeDrawable.elevation = elevation.toFloat()
            // Workaround (b/231320562): Setting background will cause drawables wrapped inside a
            // RippleDrawable lose their states, we need to reset the state here.
            materialShapeDrawable.state = jTooltipsLayout.drawableState
        }
    }

    fun getMaterialShapeDrawable(getSurfaceColorStrokeDrawable: Boolean = false): MaterialShapeDrawable? {
        val tempRippleDrawable = rippleDrawable ?: return null
        return if (tempRippleDrawable.numberOfLayers > 0) {
            val insetDrawable = tempRippleDrawable.getDrawable(0) as InsetDrawable
            val layerDrawable = insetDrawable.drawable as LayerDrawable
            return layerDrawable.getDrawable(if (getSurfaceColorStrokeDrawable) 0 else 1) as MaterialShapeDrawable
        } else null
    }

    private fun getSurfaceColorStrokeDrawable(): MaterialShapeDrawable? {
        return getMaterialShapeDrawable(true)
    }

    fun getMaskDrawable(): Shapeable? {
        val tempRippleDrawable = rippleDrawable ?: return null
        return if (tempRippleDrawable.numberOfLayers > 1) {
            if (tempRippleDrawable.numberOfLayers > 2) {
                // This is a LayerDrawable with 3 layers, so return the mask layer
                tempRippleDrawable.getDrawable(2) as Shapeable
            } else tempRippleDrawable.getDrawable(1) as Shapeable
            // This is a RippleDrawable, so return the mask layer
        } else null
    }

    /**
     * Create RippleDrawable background for Lollipop (API 21) and later API versions
     *
     * @return Drawable representing background for this button.
     */
    private fun createBackground(): Drawable? {
        val backgroundDrawable = MaterialShapeDrawable(shapeAppearanceModel)
        val context: Context = jTooltipsLayout.context
        backgroundDrawable.initializeElevationOverlay(context)
        DrawableCompat.setTintList(backgroundDrawable, backgroundTint)
        backgroundTintMode?.also {
            DrawableCompat.setTintMode(backgroundDrawable, it)
        }
        backgroundDrawable.setStroke(strokeWidth.toFloat(), strokeColor)
        val surfaceColorStrokeDrawable = MaterialShapeDrawable(shapeAppearanceModel)
        surfaceColorStrokeDrawable.setTint(Color.TRANSPARENT)
        surfaceColorStrokeDrawable.setStroke(
            strokeWidth.toFloat(),
            if (shouldDrawSurfaceColorStroke) MaterialColors.getColor(
                jTooltipsLayout,
                com.google.android.material.R.attr.colorSurface
            ) else Color.TRANSPARENT
        )
        maskDrawable = MaterialShapeDrawable(shapeAppearanceModel).apply {
            DrawableCompat.setTint(this, Color.WHITE)
        }
        rippleDrawable = RippleDrawable(
            sanitizeRippleDrawableColor(rippleColor),
            wrapDrawableWithInset(
                LayerDrawable(arrayOf<Drawable>(surfaceColorStrokeDrawable, backgroundDrawable))
            ),
            maskDrawable
        )
        return rippleDrawable
    }

    private fun wrapDrawableWithInset(drawable: Drawable): InsetDrawable {
        return InsetDrawable(drawable, insetLeft, insetTop, insetRight, insetBottom)
    }

    fun setCornerRadius(@Px cornerRadius: Float) {
        if (this.cornerRadius != cornerRadius) {
            this.cornerRadius = cornerRadius
            this.mCornerRadiusType = TypedValue.TYPE_DIMENSION
            jTooltipsLayout.removeOnLayoutChangeListener(this)
            doSetCornerRadius()
        }
    }

    fun setPercentCornerRadius(@FloatRange(from = 0.0 ,to= 1.0) cornerRadius: Float) {
        if (this.cornerRadius != cornerRadius || this.mCornerRadiusType != TypedValue.TYPE_FRACTION) {
            this.cornerRadius = cornerRadius
            this.mCornerRadiusType = TypedValue.TYPE_FRACTION
            // if view has been layout, set corner size directly
            doSetCornerRadius()
            jTooltipsLayout.addOnLayoutChangeListener(this)
        }
    }

    private fun doSetCornerRadius(){
        if (this.mCornerRadiusType == TypedValue.TYPE_NULL)
            return

        var desireCornerRadius = -1f

        if(this.mCornerRadiusType == TypedValue.TYPE_DIMENSION){
            desireCornerRadius = cornerRadius
        } else if (this.mCornerRadiusType == TypedValue.TYPE_FRACTION) {
            val currentHeight = jTooltipsLayout.measuredHeight
            if (currentHeight != 0){
                desireCornerRadius = (currentHeight - insetTop - insetBottom) * cornerRadius
            }
        }
        if(desireCornerRadius != -1f) {
            configShapeModel {
                it.setAllCornerSizes(desireCornerRadius)
            }
        }
    }

    fun setInsetTop(@Dimension newInsetTop: Int) {
        setVerticalInsets(newInsetTop, insetBottom)
    }

    fun setInsetBottom(@Dimension newInsetBottom: Int) {
        setVerticalInsets(insetTop, newInsetBottom)
    }

    fun setVerticalInsets(@Dimension newInsetTop: Int, @Dimension newInsetBottom: Int) {
        // Store padding before setting background, since background overwrites padding values
        val paddingStart = ViewCompat.getPaddingStart(jTooltipsLayout)
        val paddingTop: Int = jTooltipsLayout.paddingTop
        val paddingEnd = ViewCompat.getPaddingEnd(jTooltipsLayout)
        val paddingBottom: Int = jTooltipsLayout.paddingBottom
        val oldInsetTop = insetTop
        val oldInsetBottom = insetBottom
        insetBottom = newInsetBottom
        insetTop = newInsetTop
        if (!backgroundOverwritten) {
            updateBackground()
        }
        // Set the stored padding values
        ViewCompat.setPaddingRelative(
            jTooltipsLayout,
            paddingStart,
            paddingTop + newInsetTop - oldInsetTop,
            paddingEnd,
            paddingBottom + newInsetBottom - oldInsetBottom
        )
    }


    fun setInsetLeft(@Dimension newInsetLeft: Int) {
        setHorizontalInsets(newInsetLeft, insetRight)
    }

    fun setInsetRight(@Dimension newInsetRight: Int) {
        setHorizontalInsets(insetLeft, newInsetRight)
    }

    fun setHorizontalInsets(@Dimension newInsetLeft: Int, @Dimension newInsetRight: Int) {
        // Store padding before setting background, since background overwrites padding values
        val paddingStart = ViewCompat.getPaddingStart(jTooltipsLayout)
        val paddingTop: Int = jTooltipsLayout.paddingTop
        val paddingEnd = ViewCompat.getPaddingEnd(jTooltipsLayout)
        val paddingBottom: Int = jTooltipsLayout.paddingBottom
        val oldInsetLeft = insetLeft
        val oldInsetRight = insetRight
        insetLeft = newInsetLeft
        insetRight = newInsetRight
        if (!backgroundOverwritten) {
            updateBackground()
        }
        // Set the stored padding values
        ViewCompat.setPaddingRelative(
            jTooltipsLayout,
            paddingStart + newInsetLeft - oldInsetLeft,
            paddingTop,
            paddingEnd + newInsetRight - oldInsetRight,
            paddingBottom
        )
    }

    fun setStrokeWidth(strokeWidth: Int) {
        if (this.strokeWidth != strokeWidth) {
            this.strokeWidth = strokeWidth
            updateStroke()
        }
    }

    fun setStrokeColor(strokeColor: ColorStateList?) {
        if (this.strokeColor !== strokeColor) {
            this.strokeColor = strokeColor
            updateStroke()
        }
    }

    private fun updateStroke() {
        val materialShapeDrawable = getMaterialShapeDrawable()
        val surfaceColorStrokeDrawable = getSurfaceColorStrokeDrawable()
        if (materialShapeDrawable != null) {
            materialShapeDrawable.setStroke(strokeWidth.toFloat(), strokeColor)
            surfaceColorStrokeDrawable?.setStroke(
                strokeWidth.toFloat(),
                if (shouldDrawSurfaceColorStroke) MaterialColors.getColor(
                    jTooltipsLayout,
                    com.google.android.material.R.attr.colorSurface
                ) else Color.TRANSPARENT
            )
        }
    }

    fun setRippleColor(rippleColor: ColorStateList?) {
        if (this.rippleColor !== rippleColor) {
            this.rippleColor = rippleColor
            if (jTooltipsLayout.background is RippleDrawable) {
                (jTooltipsLayout.background as RippleDrawable)
                    .setColor(sanitizeRippleDrawableColor(rippleColor))
            }
        }
    }

    fun setBackgroundTintList(tintList: ColorStateList?) {
        if (backgroundTint !== tintList) {
            backgroundTint = tintList
            getMaterialShapeDrawable()?.also {
                DrawableCompat.setTintList(it, backgroundTint)
            }
        }
    }

    fun setBackgroundTintMode(mode: PorterDuff.Mode?) {
        if (backgroundTintMode != mode) {
            backgroundTintMode = mode
            getMaterialShapeDrawable()?.also { drawable ->
                backgroundTintMode?.also {tint ->
                    DrawableCompat.setTintMode(drawable, tint)
                }
            }
        }
    }

    fun setBackgroundColor(color: Int) {
        getMaterialShapeDrawable()?.setTint(color)
    }


    override fun onLayoutChange(
        v: View?,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        oldLeft: Int,
        oldTop: Int,
        oldRight: Int,
        oldBottom: Int,
    ) {
        if (v == null) return

        val isHeightChanged = (bottom - top) != (oldBottom - oldTop)
        if (isHeightChanged) {
            doSetCornerRadius()
        }
    }

    //<editor-fold desc="Util">
    private fun getColorStateList(
        context: Context, attributes: TypedArray, @StyleableRes index: Int
    ): ColorStateList? {
        if (attributes.hasValue(index)) {
            val resourceId = attributes.getResourceId(index, 0)
            if (resourceId != 0) {
                val value = AppCompatResources.getColorStateList(context, resourceId)
                if (value != null) {
                    return value
                }
            }
        }

        return attributes.getColorStateList(index)
    }

    /**
     * Returns a [ColorStateList] that is safe to pass to [ ].
     */
    private fun sanitizeRippleDrawableColor(rippleColor: ColorStateList?): ColorStateList {
        if (rippleColor != null) {
            return rippleColor
        }
        return ColorStateList.valueOf(Color.TRANSPARENT)
    }

    private fun parseTintMode(value: Int, defaultMode: PorterDuff.Mode): PorterDuff.Mode {
        return when (value) {
            3 -> PorterDuff.Mode.SRC_OVER
            5 -> PorterDuff.Mode.SRC_IN
            9 -> PorterDuff.Mode.SRC_ATOP
            14 -> PorterDuff.Mode.MULTIPLY
            15 -> PorterDuff.Mode.SCREEN
            16 -> PorterDuff.Mode.ADD
            else -> defaultMode
        }
    }
    //</editor-fold>
}



@Retention(AnnotationRetention.SOURCE)
@IntDef(value = [edgeStart, edgeTop, edgeEnd, edgeBottom, edgeHorizontal, edgeVertical, edgeAll])
annotation class TipEdge {
    companion object {
        const val edgeStart = 1
        const val edgeTop = 2
        const val edgeEnd = 3
        const val edgeBottom = 4
        const val edgeHorizontal = 5
        const val edgeVertical = 6
        const val edgeAll = 7
    }
}

@Retention(AnnotationRetention.SOURCE)
@IntDef(value = [styleTriangle,
        styleRoundTriangle,
        styleCircle,
        styleDefault])
annotation class TipDrawStyle {
    companion object {
        const val styleTriangle = 1
        const val styleRoundTriangle = 2
        const val styleCircle = 3
        const val styleDefault = 4
    }
}

@Retention(AnnotationRetention.SOURCE)
@IntDef(value = [drawGravityStart, drawGravityCenter, drawGravityEnd])
annotation class TipDrawGravity {
    companion object {
        const val drawGravityStart = 1
        const val drawGravityCenter = 2
        const val drawGravityEnd = 3
    }
}