package com.john6.johnbase.util.tooltips

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.withStyledAttributes
import com.john6.johnbase.R
import com.john6.johnbase.databinding.ItemDefaultTooltipsBinding
import com.john6.johnbase.util.vsp


/**
 * 便捷设置带三角指示器的CardView
 *
 * cardCornerRadius 优先级高于 shapeAppearance
 *
 * 目前只考虑**一个边**的情况
 *
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class JDefaultTooltips : JTooltipsLayout {

    val mChildBinding: ItemDefaultTooltipsBinding

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)

    @SuppressLint("ResourceType")
    constructor(context: Context, attr: AttributeSet?, defStyleInt:Int) :super(context, attr, defStyleInt){
        mChildBinding = ItemDefaultTooltipsBinding.inflate(LayoutInflater.from(context), this, true)
        context.withStyledAttributes(attr, R.styleable.JDefaultTooltips){
            this.getText(R.styleable.JDefaultTooltips_android_text)?.apply {
                mChildBinding.titleItemDt.text = this
            }
            this.getColor(R.styleable.JDefaultTooltips_android_textColor, Color.WHITE).apply {
                mChildBinding.titleItemDt.setTextColor(this)
            }
            this.getDimensionPixelSize(R.styleable.JDefaultTooltips_android_textSize,
                14.vsp.toInt()
            ).apply {
                mChildBinding.titleItemDt.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.toFloat())
            }
            this.getDimensionPixelSize(R.styleable.JDefaultTooltips_android_drawablePadding, 0).apply {
                mChildBinding.titleItemDt.compoundDrawablePadding = this
            }
            this.getDrawable(R.styleable.JDefaultTooltips_android_drawableStart)?.apply {
                mChildBinding.titleItemDt.setCompoundDrawablesRelative(this, null, null, null)
            }
            val disableTailIcon = this.getBoolean(R.styleable.JDefaultTooltips_disableTailIcon, false)
            if(disableTailIcon){
                hideOrShowTailIcon()
            }else {
                this.getDrawable(R.styleable.JDefaultTooltips_android_src)?.apply {
                    mChildBinding.btnCloseItemDt.setImageDrawable(this)
                }
            }
        }
    }

    fun hideOrShowTailIcon(hide:Boolean = true){
        if (hide) {
            mChildBinding.dividerLineItemDt.visibility = View.GONE
            mChildBinding.btnCloseItemDt.visibility = View.GONE
        }else{
            mChildBinding.dividerLineItemDt.visibility = View.VISIBLE
            mChildBinding.btnCloseItemDt.visibility = View.VISIBLE
        }
    }
}