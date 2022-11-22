package ru.netology.nmedia.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.activity.AppActivity
import ru.netology.nmedia.activity.FeedFragment
import ru.netology.nmedia.api.PostApi
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity


class PostRepositoryImpl(private val postDao: PostDao) : PostRepository {
    override val posts: LiveData<List<Post>>
        get() = postDao.getAll().map {
            it.map(PostEntity::toDto)
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

    override suspend fun getPostByIdAsync(id: Long) {
        TODO("Not yet implemented")
    }

}