package com.manigram.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.manigram.app.data.model.InstagramPost
import com.manigram.app.ui.viewmodel.MainUiState
import java.text.NumberFormat

@Composable
fun FeedScreen(state: MainUiState, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    when {
        state.loading -> Box(modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
        state.posts.isEmpty() -> EmptyLiveData(state.error, onRetry, modifier)
        else -> LazyColumn(modifier.fillMaxSize()) {
            item { StoryRow(state.posts) }
            items(state.posts, key = { it.id }) { PostCard(it) }
            state.error?.let { item { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp)) } }
        }
    }
}

@Composable private fun StoryRow(posts: List<InstagramPost>) = androidx.compose.foundation.lazy.LazyRow(
    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp), horizontalArrangement = Arrangement.spacedBy(14.dp)
) { items(posts.distinctBy { it.username }.take(12), key = { it.username }) { post ->
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(68.dp)) {
        AsyncImage(post.profilePicture, null, Modifier.size(62.dp).background(MaterialTheme.colorScheme.primary, CircleShape).padding(2.dp).clip(CircleShape), contentScale = ContentScale.Crop)
        Text(post.username, maxLines = 1, style = MaterialTheme.typography.labelSmall)
    }
} }

@Composable private fun PostCard(post: InstagramPost) = Column(modifier = Modifier.fillMaxWidth().padding(bottom = 18.dp)) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(12.dp, 6.dp)) {
        AsyncImage(post.profilePicture, null, Modifier.size(36.dp).clip(CircleShape), contentScale = ContentScale.Crop)
        Text(post.username, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 10.dp).weight(1f))
        Icon(Icons.Outlined.MoreHoriz, "Gönderi seçenekleri")
    }
    AsyncImage(post.imageUrl, "${post.username} gönderisi", Modifier.fillMaxWidth().aspectRatio(1f).background(MaterialTheme.colorScheme.surfaceVariant), contentScale = ContentScale.Crop)
    Row(Modifier.padding(horizontal = 10.dp, vertical = 5.dp)) {
        IconButton(onClick = {}) { Icon(Icons.Outlined.FavoriteBorder, "Beğen") }
        IconButton(onClick = {}) { Icon(Icons.Outlined.Send, "Paylaş") }
    }
    Text("${NumberFormat.getInstance().format(post.likes)} beğenme", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(horizontal = 14.dp))
    if (post.caption.isNotBlank()) Text("${post.username}  ${post.caption}", modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp), style = MaterialTheme.typography.bodyMedium)
}

@Composable fun EmptyLiveData(error: String?, retry: () -> Unit, modifier: Modifier = Modifier) = Column(modifier.fillMaxSize().padding(28.dp), Arrangement.Center, Alignment.CenterHorizontally) {
    Text(error ?: "Gösterilecek canlı içerik bulunamadı.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    Spacer(Modifier.height(12.dp)); Button(onClick = retry) { Text("Yeniden dene") }
}
