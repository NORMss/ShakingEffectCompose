package com.norm.myshakingeffectcompose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer


class ShakingState(
    private val strength: Strength,
    private val directions: Directions,
) {
    val xPosition = Animatable(0f)

    suspend fun shake(animationDuration: Int = 50) {
        val shakeAnimationSpec: AnimationSpec<Float> = tween(animationDuration)
        when (directions) {
            Directions.LEFT -> shakeToLeft(shakeAnimationSpec)
            Directions.RIGHT -> shakeToRight(shakeAnimationSpec)
            Directions.LEFT_THEN_RIGHT -> shakeToLeftThenRight(shakeAnimationSpec)
            Directions.RIGHT_THEN_LEFT -> shakeToRightThenLeft(shakeAnimationSpec)
        }
    }

    private suspend fun shakeToRightThenLeft(shakeAnimationSpec: AnimationSpec<Float>) {
        repeat(3) {
            xPosition.animateTo(strength.vale, shakeAnimationSpec)
            xPosition.animateTo(0f, shakeAnimationSpec)
            xPosition.animateTo(-strength.vale / 2, shakeAnimationSpec)
            xPosition.animateTo(0f, shakeAnimationSpec)
        }
    }

    private suspend fun shakeToLeftThenRight(shakeAnimationSpec: AnimationSpec<Float>) {
        repeat(3) {
            xPosition.animateTo(-strength.vale, shakeAnimationSpec)
            xPosition.animateTo(0f, shakeAnimationSpec)
            xPosition.animateTo(strength.vale / 2, shakeAnimationSpec)
            xPosition.animateTo(0f, shakeAnimationSpec)
        }
    }

    private suspend fun shakeToRight(shakeAnimationSpec: AnimationSpec<Float>) {
        repeat(3) {
            xPosition.animateTo(strength.vale, shakeAnimationSpec)
            xPosition.animateTo(0f, shakeAnimationSpec)
        }
    }

    private suspend fun shakeToLeft(shakeAnimationSpec: AnimationSpec<Float>) {
        repeat(3) {
            xPosition.animateTo(-strength.vale, shakeAnimationSpec)
            xPosition.animateTo(0f, shakeAnimationSpec)
        }
    }

    sealed class Strength(val vale: Float) {
        data object Normal : Strength(17f)
        data object Strong : Strength(40f)
        data class Custom(val strength: Float) : Strength(strength)
    }

    enum class Directions {
        LEFT, RIGHT, LEFT_THEN_RIGHT, RIGHT_THEN_LEFT
    }
}

@Composable
fun rememberShakingState(
    strength: ShakingState.Strength = ShakingState.Strength.Normal,
    directions: ShakingState.Directions = ShakingState.Directions.LEFT,
): ShakingState {
    return remember {
        ShakingState(strength, directions)
    }
}

fun Modifier.shake(
    state: ShakingState,
): Modifier {
    return graphicsLayer {
        translationX = state.xPosition.value
    }
}