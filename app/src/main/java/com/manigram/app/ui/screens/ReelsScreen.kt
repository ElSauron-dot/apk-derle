package com.manigram.app.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.manigram.app.data.model.InstagramPost

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReelsScreen(posts: List<InstagramPost>, modifier: Modifier = Modifier) {
    if (posts.isEmpty()) {
        Box(modifier.fillMaxSize(), Alignment.Center) { Text("Şu anda oynatılabilir canlı Reels videosu yok.") }
        return
    }
    val pagerState = rememberPagerState(pageCount = { posts.size })
    VerticalPager(state = pagerState, modifier = modifier.fillMaxSize()) { page -> ReelPlayer(posts[page], page == pagerState.currentPage) }
}

@Composable private fun ReelPlayer(post: InstagramPost, active: Boolean) {
    val context = LocalContext.current
    val player = remember(post.videoUrl) { ExoPlayer.Builder(context).build().apply {
        repeatMode = Player.REPEAT_MODE_ONE
        setMediaItem(MediaItem.fromUri(requireNotNull(post.videoUrl)))
        prepare()
    } }
    DisposableEffect(player) { onDispose { player.release() } }
    LaunchedEffect(active) { player.playWhenReady = active }
    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        AndroidView(factory = { PlayerView(it).apply { useController = false; this.player = player } }, modifier = Modifier.fillMaxSize())
        Column(Modifier.align(Alignment.BottomStart).padding(20.dp)) {
            Text("@${post.username}", color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleMedium)
            if (post.caption.isNotBlank()) Text(post.caption, color = MaterialTheme.colorScheme.onBackground, maxLines = 3)
        }
    }
}
