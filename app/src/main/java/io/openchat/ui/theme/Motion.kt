package io.openchat.ui.theme

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

/**
 * Motion spring tokens extracted from `_decompiled_ref/res/values/dimens.xml`
 * and Compose transitions from `re6.java`, `lhz.java`, and `hp71.java`:
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

    fun composerActionMorph(): ContentTransform =
        (scaleIn(initialScale = 0.72f, animationSpec = defaultSpatialSpring()) + fadeIn(tween(150)))
            .togetherWith(scaleOut(targetScale = 0.72f, animationSpec = fastSpatialSpring()) + fadeOut(tween(110)))

    fun destinationMorph(): ContentTransform =
        (fadeIn(tween(220)) + scaleIn(initialScale = 0.985f, animationSpec = defaultSpatialSpring()))
            .togetherWith(fadeOut(tween(140)))

    fun expandVerticalSpring(): EnterTransition =
        expandVertically(animationSpec = defaultSpatialSpring()) + fadeIn(tween(180))

    fun collapseVerticalSpring(): ExitTransition =
        shrinkVertically(animationSpec = defaultSpatialSpring()) + fadeOut(tween(140))
}

@Composable
fun Modifier.pressScaleClickable(
    pressedScale: Float = 0.94f,
    enabled: Boolean = true,
    onClick: () -> Unit,
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) pressedScale else 1f,
        animationSpec = OpenChatMotion.fastSpatialSpring(),
        label = "PressScaleAnimation",
    )
    return this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            enabled = enabled,
            onClick = onClick,
        )
}
