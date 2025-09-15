package com.rudo.rickAndMortyApp.presentation.screen.characterdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage


@Composable
fun CharacterBanner(
    imageUrl: String,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isFavoriteLocal by remember {
        mutableStateOf(isFavorite)
    }

    LaunchedEffect(isFavorite) {
        isFavoriteLocal = isFavorite
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF2A2C2E))
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Icon(
            imageVector = if (isFavoriteLocal) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "Favorite",
            tint = if (isFavoriteLocal) Color.Red else Color.White,
            modifier = Modifier
                .size(36.dp)
                .align(Alignment.BottomEnd)
                .offset(x = (-16).dp, y = (-16).dp)
                .clip(CircleShape)
                .background(Color(0xFF74D053))
                .padding(8.dp)
                .clickable {
                    isFavoriteLocal = !isFavoriteLocal
                    onToggleFavorite()
                }
        )
    }
}

@Composable
fun TitleAndMeta(name: String, species: String, type: String, origin: String, status: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.ExtraBold,
            fontStyle = FontStyle.Italic,
            color = Color.White
        )
    )
    Spacer(Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(Modifier.weight(1f)) {
            MetaRow(label = "Specie:", value = species)
            MetaRow(label = "From:", value = origin, highlight = true)
        }
        Column(Modifier.weight(1f)) {
            MetaRow(label = "Type:", value = type.ifBlank { "-" })
            MetaRow(label = "Status:", value = status, highlight = true)
        }
    }
}

@Composable
fun MetaRow(label: String, value: String, highlight: Boolean = false) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Color(0xFF9EA3A8),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            color = when (value.lowercase()) {
                "alive" -> Color(0xFF87E288)
                "dead" -> Color(0xFFFF5D5D)
                "unknown" -> Color(0xFF9EA3A8)
                else -> if (highlight) Color(0xFF74A1FF) else Color.White
            },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (highlight || value.lowercase() in listOf("alive", "dead", "unknown"))
                FontWeight.SemiBold
            else
                FontWeight.Normal
        )
    }
}

@Composable
fun EpisodesCarousel(
    episodes: List<EpisodeUi>,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text(
            text = "List of episodes:",
            color = Color(0xFF9EA3A8),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(episodes, key = { it.id }) { ep ->
                EpisodeCard(ep)
            }
        }
    }
}

@Composable
fun EpisodeCard(ep: EpisodeUi) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2C2E)),
        modifier = Modifier
            .height(56.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = ep.code,
                color = Color(0xFF74A1FF),
                fontWeight = FontWeight.SemiBold,
            )
            if (!ep.title.isNullOrBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = ep.title,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
