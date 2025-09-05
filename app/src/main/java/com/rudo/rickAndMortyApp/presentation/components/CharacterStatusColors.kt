package com.rudo.rickAndMortyApp.presentation.components

import androidx.compose.ui.graphics.Color
import com.rudo.rickAndMortyApp.domain.entity.Character

/**
 * Centralized color mapping for character status.
 * Provides consistent colors across all UI components.
 */
enum class CharacterStatusColors(
    val displayColor: Color,
    val subtitleColor: Color
) {
    ALIVE(
        displayColor = Color(0xFF87E288),
        subtitleColor = Color(0xFF4CAF50)
    ),
    DEAD(
        displayColor = Color(0xFFFF5D5D),
        subtitleColor = Color(0xFFF44336)
    ),
    UNKNOWN(
        displayColor = Color(0xFF9EA3A8),
        subtitleColor = Color(0xFF9E9E9E)
    );

    companion object {
        /**
         * Get color configuration for a character status.
         */
        fun fromStatus(status: Character.Status): CharacterStatusColors {
            return when (status) {
                Character.Status.Alive -> ALIVE
                Character.Status.Dead -> DEAD
                Character.Status.Unknown -> UNKNOWN
            }
        }
    }
}

/**
 * Extension function to get display color for Character.Status
 */
fun Character.Status.getDisplayColor(): Color {
    return CharacterStatusColors.fromStatus(this).displayColor
}

/**
 * Extension function to get subtitle color for Character.Status
 */
fun Character.Status.getSubtitleColor(): Color {
    return CharacterStatusColors.fromStatus(this).subtitleColor
}
