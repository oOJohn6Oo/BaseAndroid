package io.john6.johnbase.compose.ui

import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlin.math.abs
import kotlin.math.absoluteValue


/**
 * 限制滚动速度
 *
 * @author Liu Qiang
 * @version v
 * @since 2023-03-05
 */
@Composable
fun rememberJMaxScrollFlingBehavior(max: Float): FlingBehavior {
    val flingSpec = rememberSplineBasedDecay<Float>()
    return remember(flingSpec) {
        JScrollSpeedFlingBehavior(max, flingSpec)
    }
}

private class JScrollSpeedFlingBehavior(
    private val maxVelocity: Float,
    private val flingDecay: DecayAnimationSpec<Float>
) : FlingBehavior {
    override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
        // Prevent very fast scroll
        val newVelocity =
            if (initialVelocity > 0F) minOf(initialVelocity, maxVelocity)
            else maxOf(initialVelocity, -maxVelocity)

        return if (newVelocity.absoluteValue > 1f) {
            var velocityLeft = newVelocity
            var lastValue = 0f
            AnimationState(
                initialValue = 0f,
                initialVelocity = newVelocity,
            ).animateDecay(flingDecay) {
                val delta = value - lastValue
                val consumed = scrollBy(delta)
                lastValue = value
                velocityLeft = this.velocity
                // avoid rounding errors and stop if anything is unconsumed
                if (abs(delta - consumed) > 0.5f) this.cancelAnimation()
            }
            velocityLeft
        } else newVelocity
    }
}
