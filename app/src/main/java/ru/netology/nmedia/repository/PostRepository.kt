package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val posts: LiveData<List<Post>>
    suspend fun getAllAsync(): List<Post>
    suspend fun likeByIdAsync(id: Long)
    suspend fun dislikeByIdAsync(id: Long)
    suspend fun removeByIdAsync(id: Long)
    suspend fun saveAsync(post: Post)
    fun share(id: Long)
    suspend fun getPostByIdAsync(id: Long)


}


