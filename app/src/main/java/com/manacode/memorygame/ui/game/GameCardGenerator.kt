package com.manacode.memorygame.ui.game

import com.manacode.memorygame.R
import com.manacode.memorygame.model.GameCard
import kotlin.random.Random

    object GameCardGenerator {

        fun generateCards(): List<GameCard> {
            val images = listOf(
                R.drawable.img_0,
                R.drawable.img_1,
                R.drawable.img_2,
                R.drawable.img_3,
                R.drawable.img_4,
                R.drawable.img_5
            )

            val cards = mutableListOf<GameCard>()
            images.forEachIndexed { index, res ->
                cards.add(GameCard(id = index * 2, pairId = index, imageRes = res))
                cards.add(GameCard(id = index * 2 + 1, pairId = index, imageRes = res))
            }
            return cards.shuffled(Random(System.currentTimeMillis()))
        }
    }
