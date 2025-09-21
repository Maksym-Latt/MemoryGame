package com.manacode.memorygame.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameCard(
    val id: Int,
    val pairId: Int,
    val imageRes: Int,
    val isFaceUp: Boolean = false,
    val isMatched: Boolean = false
) : Parcelable