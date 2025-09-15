package com.rudo.rickAndMortyApp.presentation.screen.characterslist

/**
 * UI components for Characters screen.
 * Stateless composables that accept data and callbacks.
 */
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage

@Composable
fun CharactersSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    isSearching: Boolean = false,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        placeholder = { Text("Search characters...") },
        leadingIcon = {
            Icon(
                Icons.Outlined.Search,
                contentDescription = null,
                tint = Color(0xFF9EA3A8)
            )
        },
        trailingIcon = {
            if (isSearching) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = Color(0xFF2D2F33)
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = Color(0xFF2D2F33)
        ),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun CharacterCard(
    character: CharacterUi,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Local state for optimistic updates
    var isFavoriteLocal by remember(character.id) { mutableStateOf(character.isFavorite) }

    // Sync with domain state
    LaunchedEffect(character.isFavorite) {
        isFavoriteLocal = character.isFavorite
    }
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SubcomposeAsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                Color(0xFFF5F5F5),
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = Color(0xFF2D2F33)
                        )
                    }
                },
                error = {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                Color(0xFFE0E0E0),
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "?",
                            color = Color(0xFF9E9E9E),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color(0xFF2D2F33),
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = character.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = if (isFavoriteLocal) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (isFavoriteLocal) Color.Red else Color(0xFF6F767E),
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        isFavoriteLocal = !isFavoriteLocal
                        onFavoriteClick()
                    }
            )
        }
    }
}
