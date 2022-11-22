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
        TODO("Not yet implemented")
    }

    override suspend fun dislikeByIdAsync(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun removeByIdAsync(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun saveAsync(post: Post) {
        TODO("Not yet implemented")
    }

    override  fun share(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun getPostByIdAsync(id: Long) {
        TODO("Not yet implemented")
    }
    /**
    override fun getAllAsync(callback: PostRepository.GetCallback<List<Post>>) {
    )
    }

    override fun saveAsync(post: Post, callback: PostRepository.GetCallback<Post>) {
    PostApi.service.save(post)
    .enqueue(object : Callback<Post> {
    override fun onResponse(call: Call<Post>, response: Response<Post>) {
    val body = response.body() ?: throw java.lang.RuntimeException("body is null")
    try {
    callback.onSuccess(body)
    } catch (e: Exception) {
    callback.onError(e)
    }
    }

    override fun onFailure(call: Call<Post>, t: Throwable) {
    callback.onError(RuntimeException(t))
    }

    })

    }

    override fun removeByIdAsync(id: Long, callback: PostRepository.GetCallback<Unit>) {
    PostApi.service.delete(id)
    .enqueue(object : Callback<Unit> {
    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
    callback.onSuccess(Unit)
    }

    override fun onFailure(call: Call<Unit>, t: Throwable) {
    callback.onError(RuntimeException(t))
    }

    })
    }

    override fun likeByIdAsync(id: Long, callback: PostRepository.GetCallback<Long>) {
    PostApi.service.likeById(id)
    .enqueue(object : Callback<Post> {
    override fun onResponse(call: Call<Post>, response: Response<Post>) {
    if (!response.isSuccessful) {
    callback.onError(RuntimeException("Response code: ${response.code()}"))
    return
    }
    callback.onSuccess(id)
    }

    override fun onFailure(call: Call<Post>, t: Throwable) {
    callback.onError(RuntimeException(t))
    }
    })

    }

    override fun dislikeByIdAsync(id: Long, callback: PostRepository.GetCallback<Long>) {
    PostApi.service.unlikeById(id)
    .enqueue(object : Callback<Post> {
    override fun onResponse(call: Call<Post>, response: Response<Post>) {
    callback.onSuccess(id)
    }

    override fun onFailure(call: Call<Post>, t: Throwable) {
    callback.onError(RuntimeException(t))
    }
    })
    }


    override fun share(id: Long) {

    }


    override fun getPostByIdAsync(id: Long, callback: PostRepository.GetCallback<Post>) {
    PostApi.service.getPostById(id)
    .enqueue(object : Callback<Post>{
    override fun onResponse(call: Call<Post>, response: Response<Post>) {
    val body = response.body() ?: throw java.lang.RuntimeException("body is null")

    try {
    callback.onSuccess(body)
    } catch (e: Exception) {
    callback.onError(e)
    }
    }

    override fun onFailure(call: Call<Post>, t: Throwable) {
    callback.onError(RuntimeException(t))
    }
    })

    }
     */
}