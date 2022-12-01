package ru.netology.nmedia.repository


import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val posts: Flow<List<Post>>
    fun getNewerCount(id: Long): Flow<Int>
    suspend fun getNewerPosts():List<Post>
    suspend fun getAllAsync(): List<Post>
    suspend fun likeByIdAsync(id: Long)
    suspend fun dislikeByIdAsync(id: Long)
    suspend fun removeByIdAsync(id: Long)
    suspend fun saveAsync(post: Post)
    fun share(id: Long)



}


