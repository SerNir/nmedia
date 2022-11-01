package ru.netology.nmedia.model

import ru.netology.nmedia.dto.Post

data class FeedModel(
    val posts: List<Post> = emptyList(),
    val post: Post = Post(0, "","","",0),
    val loading: Boolean = false,
    val error: Boolean = false,
    val empty: Boolean = false,

    )