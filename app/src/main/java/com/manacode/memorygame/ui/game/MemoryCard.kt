package com.manacode.memorygame.ui.game

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import com.manacode.memorygame.R
import com.manacode.memorygame.model.GameCard


@Composable
fun MemoryCard(card: GameCard, onClick: () -> Unit) {
    val rotation by animateFloatAsState(
        targetValue = if (card.isFaceUp || card.isMatched) 0f else 180f,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
    )

    val scale by animateFloatAsState(
        targetValue = if (card.isFaceUp || card.isMatched) 1f else 0.95f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )

    Box(
        modifier = Modifier
            .padding(4.dp)
            .aspectRatio(1f)
            .scale(scale)
            .clickable(enabled = !card.isFaceUp && !card.isMatched) { onClick() }
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density
            }
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
    ) {
        val painter = if (rotation <= 90f) painterResource(card.imageRes)
        else painterResource(R.drawable.card_back)

        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
