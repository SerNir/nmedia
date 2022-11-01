package ru.netology.nmedia.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.dto.Post
import java.util.concurrent.TimeUnit


private val client = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .let {
        if (BuildConfig.DEBUG){
            it.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }else{
            it
        }
    }
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BuildConfig.BASE_URL)
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface PostApiService {
    @GET("posts")
    fun getPosts(): Call<List<Post>>

    @POST("posts")
    fun save(@Body post: Post): Call<Post>

    @DELETE("posts/{id}/")
    fun delete(@Path("id") id: Long): Call<Unit>

    @POST("posts/{id}/likes")
    fun likeById(@Path("id") id: Long):Call<Post>

    @DELETE("posts/{id}/likes")
    fun unlikeById(@Path("id") id: Long):Call<Post>

    @GET("posts/{id}/")
    fun getPostById(@Path("id") id: Long):Call<Post>

}

object PostApi{
    val service: PostApiService by lazy {
        retrofit.create()
    }
}