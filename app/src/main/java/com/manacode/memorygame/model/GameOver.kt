package com.manacode.memorygame.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class GameOver : Parcelable {
    @Parcelize
    object Win : GameOver()

    @Parcelize
    object Lose : GameOver()
}