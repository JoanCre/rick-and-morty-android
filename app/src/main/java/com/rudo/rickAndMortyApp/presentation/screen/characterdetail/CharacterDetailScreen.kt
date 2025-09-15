package com.rudo.rickAndMortyApp.presentation.screen.characterdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rudo.rickAndMortyApp.presentation.components.ErrorState
import com.rudo.rickAndMortyApp.presentation.components.LoadingState

private interface CharacterDetailActions {
    fun onNavigateBack()
    fun onRetry()
    fun onToggleFavorite()
}

@Composable
fun CharacterDetailScreen(
    characterId: Int,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CharacterDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(characterId) {
        viewModel.load(characterId)
    }

    val actions = object : CharacterDetailActions {
        override fun onNavigateBack() {
            onNavigateBack()
        }

        override fun onRetry() {
            viewModel.load(characterId)
        }

        override fun onToggleFavorite() {
            viewModel.toggleFavorite()
        }
    }

    CharacterDetailContent(
        state = state,
        actions = actions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterDetailContent(
    state: CharacterDetailUiState,
    actions: CharacterDetailActions,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Character Detail",
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { actions.onNavigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1F2123)
                )
            )
        },
        containerColor = Color(0xFF1F2123)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            contentAlignment = Alignment.TopStart
        ) {
            when {
                state.isLoading -> {
                    LoadingState(
                        message = "Loading character details...",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.error != null -> {
                    ErrorState(
                        title = "Error loading character",
                        subtitle = state.error,
                        onRetry = { actions.onRetry() },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.detail != null -> {
                    Column {
                        val detail = state.detail
                        CharacterBanner(
                            imageUrl = detail.image,
                            isFavorite = detail.isFavorite,
                            onToggleFavorite = { actions.onToggleFavorite() }
                        )
                        Spacer(Modifier.height(12.dp))
                        TitleAndMeta(
                            name = detail.name,
                            species = detail.species,
                            type = detail.type,
                            origin = detail.origin,
                            status = detail.status
                        )
                        Spacer(Modifier.height(12.dp))
                        EpisodesCarousel(episodes = detail.episodes)
                    }
                }
            }
        }
    }
}
