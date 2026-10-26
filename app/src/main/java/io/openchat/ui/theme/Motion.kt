package io.openchat.ui.theme

import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring

/**
 * Motion spring tokens extracted from `_decompiled_ref/res/values/dimens.xml`:
 * - Default spatial: damping = 0.9f, stiffness = 700.0f
 * - Fast spatial: damping = 0.9f, stiffness = 1400.0f
 * - Slow spatial: damping = 0.9f, stiffness = 300.0f
 * - Default effects: damping = 1.0f, stiffness = 1600.0f
 */
object OpenChatMotion {
    const val DEFAULT_SPATIAL_DAMPING = 0.9f
    const val DEFAULT_SPATIAL_STIFFNESS = 700.0f
    const val FAST_SPATIAL_DAMPING = 0.9f
    const val FAST_SPATIAL_STIFFNESS = 1400.0f
    const val SLOW_SPATIAL_DAMPING = 0.9f
    const val SLOW_SPATIAL_STIFFNESS = 300.0f
    const val DEFAULT_EFFECTS_DAMPING = 1.0f
    const val DEFAULT_EFFECTS_STIFFNESS = 1600.0f

    fun <T> defaultSpatialSpring(): SpringSpec<T> = spring(
        dampingRatio = DEFAULT_SPATIAL_DAMPING,
        stiffness = DEFAULT_SPATIAL_STIFFNESS,
    )

    fun <T> fastSpatialSpring(): SpringSpec<T> = spring(
        dampingRatio = FAST_SPATIAL_DAMPING,
        stiffness = FAST_SPATIAL_STIFFNESS,
    )

    fun <T> slowSpatialSpring(): SpringSpec<T> = spring(
        dampingRatio = SLOW_SPATIAL_DAMPING,
        stiffness = SLOW_SPATIAL_STIFFNESS,
    )
}
