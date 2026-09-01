package com.manigram.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.manigram.app.ui.viewmodel.MainUiState

@Composable
fun ExploreScreen(state: MainUiState, onSearch: (String) -> Unit, modifier: Modifier = Modifier) {
    var query by rememberSaveable { mutableStateOf(state.searchQuery) }
    Column(modifier.fillMaxSize()) {
        OutlinedTextField(value = query, onValueChange = { query = it }, singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(12.dp), placeholder = { Text("Kullanıcı ara") },
            leadingIcon = { Icon(Icons.Outlined.Search, null) },
            trailingIcon = { TextButton(onClick = { onSearch(query) }) { Text("Ara") } })
        if (state.loading && state.posts.isEmpty()) Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) { CircularProgressIndicator() }
        else LazyVerticalGrid(columns = GridCells.Fixed(3), verticalArrangement = Arrangement.spacedBy(2.dp), horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            items(state.posts, key = { it.id }) { post ->
                AsyncImage(post.imageUrl, "${post.username} içeriği", Modifier.aspectRatio(1f).background(MaterialTheme.colorScheme.surfaceVariant), contentScale = ContentScale.Crop)
            }
        }
    }
}
