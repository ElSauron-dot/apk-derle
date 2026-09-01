package com.manigram.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.manigram.app.data.model.InstagramProfile
import java.text.NumberFormat

@Composable
fun ProfileScreen(profile: InstagramProfile?, modifier: Modifier = Modifier) {
    if (profile == null) { Box(modifier.fillMaxSize(), Alignment.Center) { Text("Bir profil arayın veya canlı akışın yüklenmesini bekleyin.") }; return }
    Column(modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(profile.profilePicture, null, Modifier.size(84.dp).clip(androidx.compose.foundation.shape.CircleShape), contentScale = ContentScale.Crop)
            Spacer(Modifier.width(20.dp))
            Column(Modifier.weight(1f)) {
                Text("${NumberFormat.getInstance().format(profile.followers)}\nTakipçi", style = MaterialTheme.typography.labelLarge)
                Text("${NumberFormat.getInstance().format(profile.following)}\nTakip", style = MaterialTheme.typography.labelLarge)
            }
        }
        Text(profile.fullName.ifBlank { "@${profile.username}" }, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp))
        if (profile.biography.isNotBlank()) Text(profile.biography, modifier = Modifier.padding(16.dp, 4.dp))
        OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) { Text("Takip Et") }
        LazyVerticalGrid(columns = GridCells.Fixed(3), verticalArrangement = Arrangement.spacedBy(2.dp), horizontalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.padding(top = 14.dp).weight(1f)) {
            items(profile.posts, key = { it.id }) { post -> AsyncImage(post.imageUrl, null, Modifier.aspectRatio(1f), contentScale = ContentScale.Crop) }
        }
    }
}
