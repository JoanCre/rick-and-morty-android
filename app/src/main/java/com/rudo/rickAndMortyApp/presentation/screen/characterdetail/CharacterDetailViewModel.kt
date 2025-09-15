package com.rudo.rickAndMortyApp.presentation.screen.characterdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudo.rickAndMortyApp.domain.entity.CharacterDetail
import com.rudo.rickAndMortyApp.domain.useCase.CharactersUseCase
import com.rudo.rickAndMortyApp.domain.useCase.FavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val charactersUseCase: CharactersUseCase,
    private val favoriteUseCase: FavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterDetailUiState())
    val uiState: StateFlow<CharacterDetailUiState> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<CharacterDetailEffect>()
    val effects: SharedFlow<CharacterDetailEffect> = _effects.asSharedFlow()

    fun load(id: Int) {
        if (!_uiState.value.isLoading && _uiState.value.detail?.id == id) return

        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            runCatching {
                val detail = withContext(Dispatchers.IO) {
                    charactersUseCase.getCharacterDetail(id)
                }
                _uiState.value = _uiState.value.copy(
                    detail = detail.toUi(),
                    isLoading = false
                )
            }.onFailure { e ->
                _effects.emit(
                    CharacterDetailEffect.ShowError(
                        e.message ?: "Error loading character"
                    )
                )
                _uiState.value = _uiState.value.copy(
                    isLoading = false
                )
            }
        }
    }

    fun toggleFavorite() {
        val currentDetail = _uiState.value.detail ?: return

        val updatedDetail = currentDetail.copy(isFavorite = !currentDetail.isFavorite)
        _uiState.value = _uiState.value.copy(detail = updatedDetail)

        viewModelScope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    favoriteUseCase(currentDetail.id)
                }
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(detail = currentDetail)
                _effects.emit(
                    CharacterDetailEffect.ShowError(
                        e.message ?: "Error toggling favorite"
                    )
                )
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
