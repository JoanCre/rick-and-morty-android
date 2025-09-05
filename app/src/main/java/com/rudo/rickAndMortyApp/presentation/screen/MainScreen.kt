package com.rudo.rickAndMortyApp.presentation.screen

import androidx.compose.runtime.Composable
import com.rudo.rickAndMortyApp.presentation.screen.characterslist.CharactersScreen

/**
 * Entry screen:
 * - Delegates directly to CharactersScreen with hiltViewModel() injection
 * - Navigation frameworks call this from a NavHost
 * - Follows gula-android pattern with direct Screen composables
 */
@Composable
fun MainScreen(
    onOpenCharacter: (Int) -> Unit = {}
) {
    CharactersScreen(onOpenCharacter = onOpenCharacter)
}
