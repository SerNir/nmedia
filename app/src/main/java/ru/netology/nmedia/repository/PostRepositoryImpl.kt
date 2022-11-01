package ru.netology.nmedia.repository

import android.util.Log
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.activity.AppActivity
import ru.netology.nmedia.activity.FeedFragment
import ru.netology.nmedia.api.PostApi
import ru.netology.nmedia.dto.Post


class PostRepositoryImpl : PostRepository {

    override fun getAllAsync(callback: PostRepository.GetCallback<List<Post>>) {
        PostApi.service.getPosts()
            .enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                        return
                    }
                    val body = response.body() ?: throw java.lang.RuntimeException("body is null")
                    try {
                        callback.onSuccess(body)
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }

            })
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
                    TODO("Not yet implemented")
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
                    TODO("Not yet implemented")
                }
            })

    }
}