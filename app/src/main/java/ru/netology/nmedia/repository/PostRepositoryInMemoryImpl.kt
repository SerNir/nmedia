package ru.netology.nmedia.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {
    private var post = Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий будующего",
        content = "Привет это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растем сами и помогаем расти студентам: от новичка до уверенных профессианалов. Но самое важное остается с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия - помочь встать на путь роста и начить цепочку перемен - http://netolo.gy/fyb",
        published = "19 июля 2022",
        likedByMe = false,
        shares = 997,
        likes = 999
    )
    private val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data

    override fun like() {
        post = if (!post.likedByMe) {
            post.copy(likedByMe = true, likes = post.likes + 1)
        } else {
            post.copy(likedByMe = false, likes = post.likes - 1)
        }
        data.value = post
    }

    override fun share() {
        post = post.copy(shares = post.shares + 1)
        data.value = post
    }
}
