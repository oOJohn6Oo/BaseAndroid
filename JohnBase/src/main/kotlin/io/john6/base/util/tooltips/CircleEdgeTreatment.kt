package io.john6.base.util.tooltips

import com.google.android.material.shape.EdgeTreatment
import com.google.android.material.shape.ShapePath
import io.john6.base.util.tooltips.TipDrawGravity


/**
 * 三阶贝塞尔曲线绘制的带圆角三角指示器的Tooltips
 *
 * @param inside 是否在Shape内
 *
 */
class CircleEdgeTreatment(
    private val radius: Float = 20f,
    private val inside: Boolean = false,
    @TipDrawGravity
    private val edgeGravity: Int = TipDrawGravity.drawGravityCenter,
) : EdgeTreatment() {
    override fun getEdgePath(
        length: Float,
        center: Float,
        interpolation: Float,
        shapePath: ShapePath
    ) {

        val desireRadius = interpolation * radius
        val sweepAngle = if (inside) -180f else 180f

        val offset = length / 2 - center
        val startX = when (edgeGravity) {
            TipDrawGravity.drawGravityStart -> {
                0f - offset
            }
            TipDrawGravity.drawGravityEnd -> {
                length - desireRadius - offset
            }
            else -> {
                center - desireRadius
            }
        }

        shapePath.lineTo(startX, 0f)
        shapePath.addArc(
            startX,
            -desireRadius,
            startX + desireRadius * 2,
            desireRadius,
            180f,
            sweepAngle
        )
        shapePath.lineTo(length, 0f)
    }
}