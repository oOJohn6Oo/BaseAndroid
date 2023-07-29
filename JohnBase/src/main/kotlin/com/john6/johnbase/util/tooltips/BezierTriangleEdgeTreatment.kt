package com.john6.johnbase.util.tooltips

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.material.shape.EdgeTreatment
import com.google.android.material.shape.ShapePath
import com.john6.johnbase.util.vdp


/**
 * 三阶贝塞尔曲线绘制的带圆角三角指示器的Tooltips
 *
 * @param width 三角形 底边长度
 * @param height 三角形 高
 * @param inside 是否在Shape内
 *
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class BezierTriangleEdgeTreatment(
    private val width: Float = 10.vdp,
    private val height: Float = 4.vdp,
    private val inside: Boolean = false,
    @TipDrawGravity
    private val edgeGravity: Int = TipDrawGravity.drawGravityCenter,
):EdgeTreatment() {
    override fun getEdgePath(
        length: Float,
        center: Float,
        interpolation: Float,
        shapePath: ShapePath
    ) {
        val desireHalfW = interpolation* width /2f
        val desireH = 1.25f * interpolation * height * if (inside) 1 else -1

        val offset = length / 2 - center

        val startX = when (edgeGravity) {
            TipDrawGravity.drawGravityStart -> {
                0 - offset
            }
            TipDrawGravity.drawGravityEnd -> {
                length - desireHalfW * 2 - offset
            }
            else -> {
                center - desireHalfW
            }
        }

        val controlX = startX + desireHalfW

        shapePath.lineTo(startX, 0f)
        shapePath.cubicToPoint(controlX, desireH, controlX, desireH, controlX + desireHalfW, 0f)
        shapePath.lineTo(length, 0f)
    }
}