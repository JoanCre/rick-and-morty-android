package com.rudo.rickAndMortyApp.presentation.screen.characterslist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.rudo.rickAndMortyApp.presentation.components.EmptyState
import com.rudo.rickAndMortyApp.presentation.components.ErrorState
import com.rudo.rickAndMortyApp.presentation.components.LoadingState
import com.rudo.rickAndMortyApp.presentation.components.PagingErrorState
import com.rudo.rickAndMortyApp.presentation.components.PagingLoadingState

private interface CharactersActions {
    fun onNavigateToCharacterDetail(id: Int)
    fun onTabChange(tab: CharacterTab)
    fun onQueryChange(query: String)
    fun onFavoriteToggle(id: Int)
}

/**
 * CharactersScreen
 *
 * - Injects the ViewModel via Hilt using [hiltViewModel] because the screen should not construct its own dependencies.
 * - Collects Flow<PagingData<CharacterUi>> as [LazyPagingItems] with [collectAsLazyPagingItems] because Paging 3 integrates with Compose through this API.
 * - Delegates user events through [CharactersActions] because the UI should be dumb and forward intents to the ViewModel (gula-android pattern).
 */
@Composable
fun CharactersScreen(
    onOpenCharacter: (Int) -> Unit,
    viewModel: CharactersViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()
    val items = viewModel.items.collectAsLazyPagingItems()

    val actions = object : CharactersActions {
        override fun onNavigateToCharacterDetail(id: Int) {
            onOpenCharacter(id)
        }

        override fun onTabChange(tab: CharacterTab) {
            viewModel.onEvent(CharactersEvent.OnTabChange(tab))
        }

        override fun onQueryChange(query: String) {
            viewModel.onEvent(CharactersEvent.OnQueryChange(query))
        }

        override fun onFavoriteToggle(id: Int) {
            viewModel.onEvent(CharactersEvent.OnFavoriteToggle(id))
        }
    }

    CharactersContent(
        uiState = uiState,
        items = items,
        actions = actions,
        modifier = modifier
    )
}

/**
 * CharactersContent
 *
 * - Renders a paged list using [LazyColumn] and [LazyPagingItems] because Paging 3 exposes data in pages for performance.
 * - Handles Paging 3 load states via [items.loadState] because the UI must reflect loading, error, and empty states:
 *   - refresh: Loading/Error/Empty for the initial load
 *   - append: Loading/Error for loading more items at the end
 * - Keeps only presentation logic here because business/data logic belongs in ViewModel or data layers.
 */
@Composable
private fun CharactersContent(
    uiState: CharactersUiState,
    items: LazyPagingItems<CharacterUi>,
    actions: CharactersActions,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1F2123))
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = "RICKY AND MORTY",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            ),
        )

        Spacer(Modifier.height(16.dp))

        CharactersSearchBar(
            query = uiState.query,
            onQueryChange = { actions.onQueryChange(it) },
            isSearching = uiState.isSearching,
        )

        Spacer(Modifier.height(16.dp))

        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                onClick = { actions.onTabChange(CharacterTab.ALL) },
                selected = uiState.selectedTab == CharacterTab.ALL
            ) {
                Text("All Characters")
            }
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                onClick = { actions.onTabChange(CharacterTab.FAVORITES) },
                selected = uiState.selectedTab == CharacterTab.FAVORITES
            ) {
                Text("Favorites")
            }
        }

        Spacer(Modifier.height(16.dp))

        when {
            items.loadState.refresh is LoadState.Loading -> {
                LoadingState(message = "Loading characters...")
            }

            items.loadState.refresh is LoadState.Error -> {
                ErrorState(
                    title = "Error loading characters",
                    subtitle = "Please try again"
                )
            }

            items.loadState.refresh is LoadState.NotLoading && items.itemCount == 0 -> {
                val (title, subtitle) = when {
                    uiState.selectedTab == CharacterTab.FAVORITES && uiState.query.isBlank() ->
                        "No favorites yet" to "Add characters to favorites to see them here"

                    uiState.selectedTab == CharacterTab.FAVORITES && uiState.query.isNotBlank() ->
                        "No favorite characters found" to "Try a different search term"

                    uiState.query.isNotBlank() ->
                        "Characters not found" to "Try a different search term"

                    else ->
                        "No characters available" to "Check your connection"
                }
                EmptyState(title = title, subtitle = subtitle)
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        count = items.itemCount,
                        key = { index -> items[index]?.id ?: index }
                    ) { index ->
                        val item = items[index]
                        if (item != null) {
                            CharacterCard(
                                character = item,
                                onFavoriteClick = { actions.onFavoriteToggle(item.id) },
                                modifier = Modifier.clickable {
                                    actions.onNavigateToCharacterDetail(
                                        item.id
                                    )
                                }
                            )
                        }
                    }

                    item {
                        when (items.loadState.append) {
                            is LoadState.Loading -> {
                                PagingLoadingState()
                            }

                            is LoadState.Error -> {
                                PagingErrorState()
                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

