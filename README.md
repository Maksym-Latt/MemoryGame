# Memory Game

Simple Android game "Find a Pair" built with Kotlin and Jetpack Compose.

## Description

Memory Game is a mobile app where the player finds matching pairs of cards. The game consists of:

- **Game Screen:** 12 cards (6 pairs), initially revealed for 2 seconds, then hidden. The player flips two cards at a time:
    - If they match → stay open
    - If they don’t match → close after 1 second, lose 1 life
- **Lives:** 5
- **Win condition:** all pairs found
- **Lose condition:** lives reach 0

- **History Screen:** Shows past games with:
    - Number of pairs found
    - Result: Win/Lose
    - Date and time of the game
