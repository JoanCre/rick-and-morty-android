package com.rudo.rickAndMortyApp.presentation.screen.characterdetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudo.rickAndMortyApp.domain.entity.CharacterDetail
import com.rudo.rickAndMortyApp.domain.useCase.Favorite.FavoriteUseCase
import com.rudo.rickAndMortyApp.domain.useCase.GetCharacterDetail.GetCharacterDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val getCharacterDetail: GetCharacterDetailUseCase,
    private val favoriteUseCase: FavoriteUseCase
) : ViewModel() {

    var uiState: CharacterDetailUiState by mutableStateOf(CharacterDetailUiState())
        private set

    fun load(id: Int) {
        if (!uiState.isLoading && uiState.detail?.id == id) return
        uiState = uiState.copy(isLoading = true, error = null)
        viewModelScope.launch {
            runCatching { getCharacterDetail(id) }
                .onSuccess { detail ->
                    uiState = CharacterDetailUiState(
                        isLoading = false,
                        error = null,
                        detail = detail.toUi()
                    )
                }
                .onFailure { e ->
                    uiState = CharacterDetailUiState(
                        isLoading = false,
                        error = e.message ?: "Unknown error",
                        detail = null
                    )
                }
        }
    }

    fun toggleFavorite() {
        val currentDetail = uiState.detail ?: return
        viewModelScope.launch {
            favoriteUseCase(currentDetail.id)
                .onSuccess {
                    // Update UI state immediately for better UX
                    uiState = uiState.copy(
                        detail = currentDetail.copy(isFavorite = !currentDetail.isFavorite)
                    )
                }
                .onFailure { e ->
                    // Handle error if needed - could show a snackbar
                    uiState = uiState.copy(error = e.message ?: "Error toggling favorite")
                }
        }
    }
}

private fun CharacterDetail.toUi(): CharacterDetailUi = CharacterDetailUi(
    id = id,
    name = name,
    image = image,
    species = species,
    type = type,
    origin = originName,
    status = status.name,
    episodes = episodes.map { ep ->
        val code = "S" + ep.id.toString().padStart(2, '0') + "E" + ep.id.toString().padStart(2, '0')
        EpisodeUi(id = ep.id, code = code)
    },
    isFavorite = isFavorite
)
