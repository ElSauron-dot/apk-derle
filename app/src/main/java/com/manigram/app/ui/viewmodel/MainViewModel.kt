package com.manigram.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manigram.app.data.model.InstagramPost
import com.manigram.app.data.model.InstagramProfile
import com.manigram.app.data.repository.InstagramRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class MainUiState(
    val loading: Boolean = true,
    val posts: List<InstagramPost> = emptyList(),
    val profile: InstagramProfile? = null,
    val searchedProfile: InstagramProfile? = null,
    val error: String? = null,
    val searchQuery: String = ""
)

class MainViewModel(private val repository: InstagramRepository = InstagramRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
    private val starterAccounts = listOf("natgeo", "nasa", "nike")

    init { loadInitialFeed() }

    fun loadInitialFeed() = launchRequest { current ->
        val profiles = starterAccounts.mapNotNull { runCatching { repository.getProfile(it) }.getOrNull() }
        if (profiles.isEmpty()) error("Canlı akış şu anda alınamadı. Ağ bağlantınızı kontrol edip yeniden deneyin.")
        current.copy(posts = profiles.flatMap { it.posts }, profile = profiles.firstOrNull())
    }

    fun search(username: String) {
        val clean = username.trim().removePrefix("@")
        if (clean.isBlank()) return
        launchRequest { current ->
            val profile = repository.getProfile(clean)
            current.copy(searchedProfile = profile, searchQuery = clean)
        }
    }

    fun clearError() { _uiState.value = _uiState.value.copy(error = null) }

    private fun launchRequest(block: suspend (MainUiState) -> MainUiState) {
        viewModelScope.launch {
            val previous = _uiState.value
            _uiState.value = previous.copy(loading = true, error = null)
            runCatching { withContext(Dispatchers.IO) { block(previous) } }
                .onSuccess { _uiState.value = it.copy(loading = false) }
                .onFailure { _uiState.value = previous.copy(loading = false, error = it.message ?: "İstek tamamlanamadı.") }
        }
    }
}
