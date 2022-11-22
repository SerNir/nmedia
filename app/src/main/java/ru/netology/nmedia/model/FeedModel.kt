package ru.netology.nmedia.model

import ru.netology.nmedia.dto.Post

data class FeedModel(
    val posts: List<Post> = emptyList(),
    val empty: Boolean = false,
)

sealed interface FeedModeState {
    object Error : FeedModeState
    object Loading : FeedModeState
    object Refreshing : FeedModeState
    object Idle : FeedModeState
}