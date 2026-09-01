package com.manigram.app.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SmartDisplay
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.manigram.app.ui.viewmodel.MainViewModel

private enum class Tab(val label: String) { FEED("Akış"), EXPLORE("Keşfet"), REELS("Reels"), PROFILE("Profil") }

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()
    var tab by rememberSaveable { mutableStateOf(Tab.FEED) }
    Scaffold(
        topBar = { ManigramTopBar(onSearch = { tab = Tab.EXPLORE }) },
        bottomBar = { NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
            Tab.entries.forEach { item ->
                val icon = when (item) { Tab.FEED -> Icons.Outlined.Home; Tab.EXPLORE -> Icons.Outlined.Search; Tab.REELS -> Icons.Outlined.SmartDisplay; Tab.PROFILE -> Icons.Outlined.PersonOutline }
                NavigationBarItem(selected = tab == item, onClick = { tab = item }, icon = { Icon(icon, item.label) }, label = { Text(item.label) })
            }
        } }
    ) { padding ->
        when (tab) {
            Tab.FEED -> FeedScreen(state, onRetry = viewModel::loadInitialFeed, modifier = Modifier.padding(padding))
            Tab.EXPLORE -> ExploreScreen(state, onSearch = viewModel::search, modifier = Modifier.padding(padding))
            Tab.REELS -> ReelsScreen(posts = state.posts.filter { it.isVideo && !it.videoUrl.isNullOrBlank() }, modifier = Modifier.padding(padding))
            Tab.PROFILE -> ProfileScreen(state.searchedProfile ?: state.profile, modifier = Modifier.padding(padding))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable private fun ManigramTopBar(onSearch: () -> Unit) = TopAppBar(
    title = { Text("Manigram", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.headlineSmall) },
    actions = {
        IconButton(onClick = {}) { Icon(Icons.Outlined.NotificationsNone, "Bildirimler") }
        IconButton(onClick = onSearch) { Icon(Icons.Outlined.Search, "Ara") }
    }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
)
