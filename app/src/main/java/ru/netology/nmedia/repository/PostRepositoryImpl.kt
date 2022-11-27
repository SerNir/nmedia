package ru.netology.nmedia.repository

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.netology.nmedia.api.PostApi
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.entity.toEntity
import java.io.IOException


class PostRepositoryImpl(private val postDao: PostDao) : PostRepository {
    override val posts: Flow<List<Post>>
        get() = postDao.getAll().map {
            it.map(PostEntity::toDto)
        }.flowOn(Dispatchers.Default)



    override fun getNewerCount(id: Long): Flow<Int> = flow {
        while (true) {
            try {
                delay(10_000)
                val response = PostApi.service.getNewer(id)
                if (!response.isSuccessful) {
                    throw RuntimeException("Response code: ${response.code()}")
                }

                val body = response.body() ?: throw java.lang.RuntimeException("body is null")
                postDao.insert(body.toEntity().map {it.copy(showed = false)})
                emit(body.size)
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
//                no operation
            }
        }
    }

    override suspend fun getAllAsync(): List<Post> {
        val response = PostApi.service.getPosts()
        if (!response.isSuccessful) {
            throw RuntimeException("Response code: ${response.code()}")

        }
        return response.body()?.also {
            postDao.insert(it.map(PostEntity::fromDto))
        } ?: throw java.lang.RuntimeException("body is null")
    }

    override suspend fun likeByIdAsync(id: Long) {
        val response = PostApi.service.likeById(id)
        if (!response.isSuccessful) {
            throw java.lang.RuntimeException("Response code: ${response.code()}")
        }
        response.body()?.also {
            postDao.insert(PostEntity.fromDto(it))
        }
    }

    override suspend fun dislikeByIdAsync(id: Long) {
        val response = PostApi.service.unlikeById(id)
        if (!response.isSuccessful) {
            throw java.lang.RuntimeException("Response code: ${response.code()}")
        }
        response.body()?.also {
            postDao.insert(PostEntity.fromDto(it))
        }
    }

    override suspend fun removeByIdAsync(id: Long) {
        val response = PostApi.service.delete(id)
        if (!response.isSuccessful) {
            throw java.lang.RuntimeException("Response code: ${response.code()}")
        }
        response.body()?.also {
            postDao.removeById(id)
        }
    }

    override suspend fun saveAsync(post: Post) {
        val response = PostApi.service.save(post)
        if (!response.isSuccessful) {
            throw java.lang.RuntimeException("Response code: ${response.code()}")
        }
        response.body()?.also {
            postDao.save(PostEntity.fromDto(post))
        }
    }

    override fun share(id: Long) {

    }

}